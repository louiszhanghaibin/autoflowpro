package cmsz.autoflow.engine.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsz.autoflow.engine.helper.XmlHelper;
import cmsz.autoflow.engine.model.EndModel;
import cmsz.autoflow.engine.model.FieldModel;
import cmsz.autoflow.engine.model.ForkModel;
import cmsz.autoflow.engine.model.JoinModel;
import cmsz.autoflow.engine.model.NodeModel;
import cmsz.autoflow.engine.model.ProcessModel;
import cmsz.autoflow.engine.model.StartModel;
import cmsz.autoflow.engine.model.TaskModel;
import cmsz.autoflow.engine.model.TransitionModel;

public class ModelParser {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelParser.class);

	/**
	 * 解析流程定义文件，并将解析后的对象放入模型容器中
	 * @param bytes
	 * @return
	 */
	public static ProcessModel parse(byte[] bytes) {
		DocumentBuilder documentBuilder = XmlHelper.createDocumentBuilder();
		
		if (null == documentBuilder) {
			return null;
		}

		try {
			Document doc = documentBuilder.parse(new ByteArrayInputStream(bytes));
			Element processE = doc.getDocumentElement();//此处获得的是document的根节点
			ProcessModel process = new ProcessModel();
			process.setName(processE.getAttribute(NodeParser.ATTR_NAME));
			process.setId(processE.getAttribute(NodeParser.ATTR_ID));
			NodeList nodeList = processE.getChildNodes();//获得根节点的子节点列表
			int nodeSize = nodeList.getLength();
			for (int i = 0; i < nodeSize; i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					NodeModel model = parseModel(node);
					process.getNodes().add(model);
				}
			}

			// 循环节点模型，构造变迁输入、输出的source、target
			for (NodeModel node : process.getNodes()) {
				for (TransitionModel transition : node.getOutputs()) {
					String to = transition.getTo();
					for (NodeModel node2 : process.getNodes()) {
						if (to.equalsIgnoreCase(node2.getName())) {
							node2.getInputs().add(transition);
							transition.setTarget(node2);
						}
					}
				}
			}
			return process;
		} catch (SAXException|IOException e) {
			logger.warn("流程文件解析出现错误",e);
		}
	
		return null;
	}

	private static NodeModel parseModel(Node node) {
		String nodeName = node.getNodeName();
		logger.debug("parser node : "+ nodeName);
		Element element = (Element) node;
		logger.debug("node name : "+element.getAttribute(NodeParser.ATTR_NAME));
		switch (nodeName) {
		case NodeParser.ELEM_START:
			return getStartModel(element);
		case NodeParser.ELEM_END:
			return getEndModel(element);
		case NodeParser.ELEM_TASK:
			return getTaskModel(element);
		case NodeParser.ELEM_FORK:
			return getForkModel(element);
		case NodeParser.ELEM_JOIN:
			return getJoinModel(element);
		default:
			logger.debug("Element "+nodeName+" can't reconizeg");
			return null;
		}
	}

	private static TaskModel getTaskModel(Element element) {
		TaskModel model = new TaskModel();
		model.setName(element.getAttribute(NodeParser.ATTR_NAME));
		model.setLauncher(element.getAttribute(NodeParser.ATTR_LAUNCHER));
		model.setRefBean(element.getAttribute(NodeParser.ATTR_REFBEAN));
		model.setRefClass(element.getAttribute(NodeParser.ATTR_REFCLASS));
		model.setRefDubbo(element.getAttribute(NodeParser.ATTR_REFDUBBO));
		model.setRefComponent(element.getAttribute(NodeParser.ATTR_COMPONENT));
		
		List<FieldModel> fieldList = getFieldModelList(element);
		model.setFieldList(fieldList);
		
		List<TransitionModel> tranlist = getTransitionModelList(element);
		for (TransitionModel t : tranlist) {
			t.setSource(model);
		}
		model.getOutputs().addAll(tranlist);
		return model;
	}

	private static EndModel getEndModel(Element element) {
		EndModel model = new EndModel();
		model.setName(element.getAttribute(NodeParser.ATTR_NAME));
		return model;
	}

	private static StartModel getStartModel(Element element) {
		StartModel model = new StartModel();
		model.setName(element.getAttribute(NodeParser.ATTR_NAME));
		List<TransitionModel> tranlist = getTransitionModelList(element);
		for (TransitionModel t : tranlist) {
			t.setSource(model);
		}
		model.getOutputs().addAll(tranlist);
		return model;
	}
	
	private static ForkModel getForkModel(Element element)
	{
		ForkModel model=new ForkModel();
		model.setName(element.getAttribute(NodeParser.ATTR_NAME));
		List<TransitionModel> tranlist = getTransitionModelList(element);
		for (TransitionModel t : tranlist) {
			t.setSource(model);
		}
		model.getOutputs().addAll(tranlist);
		return model;
	}
	
	private static JoinModel getJoinModel(Element element)
	{
		JoinModel model=new JoinModel();
		model.setName(element.getAttribute(NodeParser.ATTR_NAME));
		List<TransitionModel> tranlist = getTransitionModelList(element);
		for (TransitionModel t : tranlist) {
			t.setSource(model);
		}
		model.getOutputs().addAll(tranlist);
		return model;
	}

	private static TransitionModel getTransitionModel(Element elem) {
		TransitionModel model = new TransitionModel();
		logger.debug("parser transition : "+elem.getAttribute(NodeParser.ATTR_NAME));
		logger.debug("parser transition to : "+elem.getAttribute(NodeParser.ATTR_TO));
		model.setName(elem.getAttribute(NodeParser.ATTR_NAME));
		model.setTo(elem.getAttribute(NodeParser.ATTR_TO));
		return model;
	}

	private static List<TransitionModel> getTransitionModelList(Element elem) {
		List<TransitionModel> list = new ArrayList<>();
		NodeList nodeList = elem.getChildNodes();
		int nodeSize = nodeList.getLength();
		for (int i = 0; i < nodeSize; i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element subelem = (Element) node;
				if (subelem.getTagName().equals(NodeParser.ELEM_TRAN)) {
					list.add(getTransitionModel(subelem));
				}
			}
		}
		return list;
	}
	
	private static List<FieldModel> getFieldModelList(Element elem) {
		List<FieldModel> list = new ArrayList<>();
		NodeList nodeList = elem.getChildNodes();
		int nodeSize = nodeList.getLength();
		for (int i = 0; i < nodeSize; i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element subelem = (Element) node;
				if (subelem.getTagName().equals(NodeParser.ELEM_FIELD)) {
					list.add(getFieldModel(subelem));
				}
			}
		}
		return list;
	}
	
	private static FieldModel getFieldModel(Element elem) {
		FieldModel model = new FieldModel();
		logger.debug("parser field key : "+elem.getAttribute(NodeParser.ATTR_KEY));
		logger.debug("parser field value : "+elem.getAttribute(NodeParser.ATTR_VALUE));
		model.setKey(elem.getAttribute(NodeParser.ATTR_KEY));
		model.setValue(elem.getAttribute(NodeParser.ATTR_VALUE));
		return model;
	}
}
