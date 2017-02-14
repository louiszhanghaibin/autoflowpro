package cmsz.autoflow.engine;

import java.util.List;
import java.util.Map;

import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.entity.FlowAppend;
import cmsz.autoflow.engine.entity.Process;

/**
 * 流程实例服务接口
 * 实现 创建、保存、查找流程实例
 * @author zhoushuang
 */

public interface IFlowService {
	
	/**
	 * 根据流程定义和参数创建流程实例,并在数据库中插入数据.
	 * @param process   流程定义
	 * @param args		参数
	 * @return	流程实例
	 */
	Flow createFlow(Process process, Map<String, Object> args);
	
	/**
	 * 将流程实例保存到数据库
	 * @param flow
	 */
	void saveFlow(Flow flow);
	
	/**
	 * 流程实例结束,仅更新状态
	 * @param flowId
	 */
	void update(String flowId, String state);
	
	
//----------------- Query ------------------
	/**
	 * 从流程实例表中读出数据
	 * @param flowId	流程实例id   
	 * @return
	 */
	Flow getFlow(String flowId);
	
	List<Flow> getFlow(String flowName, String settleDate);

	Flow createFlow(Process process, Map<String, Object> args, String flowId, String flowName);
	
	void updateFlowAppend(FlowAppend flowAppend);
	
}
