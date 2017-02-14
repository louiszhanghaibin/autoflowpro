package cmsz.autoflow.engine.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.Constant;
import cmsz.autoflow.engine.IFlowService;
import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.entity.FlowAppend;
import cmsz.autoflow.engine.entity.Process;
import cmsz.autoflow.engine.helper.DateHelper;
import cmsz.autoflow.engine.helper.JsonHelper;
import cmsz.autoflow.engine.helper.SequenceHelper;
import cmsz.autoflow.engine.helper.StringHelper;

/**
 * FlowService 实现IFlowService, 继承AccessService抽象类，对数据库进行操作
 * 
 * @author zhoushuang
 * 
 */
public class FlowService extends AccessService implements IFlowService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Flow createFlow(Process process, Map<String, Object> args) {
		
		return createFlow(process, args, null, null);
		
//		logger.debug("createFlow process: {}", process.toString());
//		Flow flow = new Flow();
//		flow.setId(StringHelper.getPrimaryKey());
//		flow.setProcessId(process.getId());
//		flow.setProcessName(process.getName());
//		flow.setCreateTime(DateHelper.getTime());
//		flow.setUpdateTime(flow.getCreateTime());
//		flow.setVariables(JsonHelper.toJson(args));
//		flow.setState(Flow.STATE_ACTIVE);
//		saveFlow(flow);
//		
//		logger.debug("createFlow flow:{}", flow.toString());
//		return flow;
	}
	
	
	@Override
	public Flow createFlow(Process process, Map<String, Object> args,
			String flowId, String flowName) {
		
		logger.debug("createFlow process: {}", process.toString());
		Flow flow = new Flow();
		
		if(StringHelper.isNotEmpty(flowId))
			flow.setId(flowId);
		else
			flow.setId("FLOW-"+DateHelper.getDate(0) + "-"+ SequenceHelper.getSequence());
		
		if(StringHelper.isNotEmpty(flowName))
			flow.setName(flowName);
		else
			flow.setName(flow.getId());
		
		flow.setProcessId(process.getId());
		flow.setProcessName(process.getName());
		flow.setCreateTime(DateHelper.getTime());
		flow.setUpdateTime(flow.getCreateTime());
		flow.setVariables(JsonHelper.toJson(args));
		flow.setState(Constant.State.ACTIVE);
		saveFlow(flow);
		
		logger.debug("createFlow flow:{}", flow.toString());
		return flow;
		
	}
	

	@Override
	public void saveFlow(Flow flow) {
		access().saveFlow(flow);
	}

	@Override
	public void update(String flowId, String state) {
		Flow flow = getFlow(flowId);
		flow.setUpdateTime(DateHelper.getTime());
		flow.setState(state);
		if(state.equals(Constant.State.FINISH))
			flow.setFinishTime(flow.getUpdateTime());
		access().updateFlow(flow);
	}

	/**
	 * 根据流程实例Id取出流程实例
	 */
	@Override
	public Flow getFlow(String id) {
		return access().getFlow(id);
	}


	@Override
	public void updateFlowAppend(FlowAppend flowAppend) {
		access().updateFlowAppend(flowAppend);
	}


	@Override
	public List<Flow> getFlow(String flowName, String settleDate) {
		return access().getFlow(flowName, settleDate);
	}



}
