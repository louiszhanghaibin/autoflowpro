package cmsz.autoflow.engine;

import java.util.Map;

import cmsz.autoflow.engine.cfg.Configuration;
import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.entity.Task;
import cmsz.autoflow.engine.eventhandlers.IEventService;
import cmsz.autoflow.engine.thread.IThreadService;

public interface AutoEngine {
	
	/**
	 * 配置QuickEngine;
	 * @param cfg
	 * @return
	 */
	public AutoEngine configure(Configuration cfg);
	
	/**
	 * 获取IProcessService对象
	 * @return
	 */
	public IProcessService process();
	
	/**
	 * 获取IFlowService对象
	 * @return
	 */
	public IFlowService flow();
	
	/**
	 * 获取ITaskService对象
	 * @return
	 */
	public ITaskService task();
	
	
	public IPlanService plan();
	
	
	public IThreadService thread();
	
	
	public IEventService event();
	
	/**
	 * 根据流程定义id, 参数，创建流程实例
	 * @param id
	 * @param args
	 * @return
	 */
	public Flow startInstanceById(String id, Map<String, Object> args);
	
	/**
	 * 根据流程定义id、 参数、流程实例Id，创建流程实例
	 * @param id	流程定义id
	 * @param args	参数
	 * @param flowId  实例id, 必须唯一
	 * @return
	 */
	public Flow startInstanceById(String id, Map<String, Object> args, String flowId, String flowName);
	
	/**
	 * 完成任务
	 * @param id	 任务主键 id
	 * @param state	 任务状态 
	 * @return
	 */
	public Task complete(String id, String state);
	
	public Task complete(String id, String state, Map<String, Object> args);
	
	
	/**
	 * 根据流程实例id重做流程
	 * @param id	流程实例id
	 * @return		被重做的流程实例对象， 如果流程实例不存在，则返回null
	 */
	public Flow redoFlow(String id);
	
	/**
	 * 当任务执行失败后， 根据任务实例id重新启动任务。
	 * @param id	任务实例id
	 * @return		被重启动的任务实例，如果任务不存在，则返回null
	 */
	public Task redoTask(String id);
}
