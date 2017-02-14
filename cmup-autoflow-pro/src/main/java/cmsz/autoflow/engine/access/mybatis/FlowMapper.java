package cmsz.autoflow.engine.access.mybatis;

import java.util.List;
import java.util.Map;

import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.entity.FlowAppend;

public interface FlowMapper {
	
	public void insertFlow(Flow flow);
	
	public Flow selectFlowById(String flowId);
	
	public void updateFlowState(Flow flow);
	
	public void updateFlowAppend(FlowAppend flowAppend);
	
	public List<Flow>	getFlowSelective(Map<String, Object> paraMap);
	
}
