package cmsz.autoflow.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义process模型
 * 
 * @author zhoushuang
 * 
 */

public class ProcessModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4981004197492141369L;
	
	private String id;
	
	/**
	 * 节点元素集合
	 */
	private List<NodeModel> nodes = new ArrayList<NodeModel>();

	public List<NodeModel> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeModel> nodes) {
		this.nodes = nodes;
	}

	private List<TaskModel> tasks = new ArrayList<TaskModel>();

	/**
	 * lock
	 */
	private Object lock = new Object();

	/**
	 * 获取开始节点
	 * 
	 * @return
	 */
	public StartModel getStart() {
		for (NodeModel node : nodes) {
			if (node instanceof StartModel)
				return (StartModel) node;
		}
		return null;
	}

	/**
	 * 返回指定名字的NodeModel
	 * 
	 * @param nodename
	 * @return
	 */

	public NodeModel getNode(String nodename) {
		for (NodeModel node : nodes) {
			if (node.getName().equals(nodename)) {
				return node;
			}
		}
		return null;
	}
	
	
	/**
	 * 根据指定的节点类型返回流程定义中所有模型对象
	 * @param clazz
	 * @return
	 */

	public <T> List<T> getModels(Class<T> clazz) {
		List<T> models = new ArrayList<T>();
		buildModels(models, this.getStart().getNextModels(clazz), clazz);
		return null;
	}

	private <T> void buildModels(List<T> models, List<T> nextModels,
			Class<T> clazz) {
		for (T nextModel : nextModels) {
			if (!models.contains(nextModel)) {
				models.add(nextModel);
				buildModels(models,
						((NodeModel) nextModel).getNextModels(clazz), clazz);
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
