package cmsz.autoflow.engine;

import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.entity.Task;
import cmsz.autoflow.engine.entity.TaskAppend;
import cmsz.autoflow.engine.model.TaskModel;

/**
 * 任务实例服务接口
 * 用于创建任务、保存任务、查找任务等
 * @author zhoushuang
 *
 */

public interface ITaskService {
	
	/**
	 * 保存任务，在数据库中插入数据
	 * @param task
	 */
	public void saveTask(Task task);
	
	/**
	 * 根据TaskModel， Execution创建任务
	 * @param model
	 * @param execution
	 * @return
	 */
	public Task createTask(TaskModel model, Execution execution);
	
	
	/**
	 * 根据TaskId， 任务状态更新数据库状态
	 * @param id
	 * @param state
	 * @return
	 */
	
//	public Task update(String id, String state);
	
	/**
	 * 更新task
	 * @param task
	 */
	public void updateTask(Task task);
	
	
//----------------- Query ------------------------	
	/**
	 * 根据TaskId从数据库获取Task对象
	 * @param id
	 * @return
	 */
	public Task getTask(String taskId);
	
	/**
	 * 根据流程实例名、任务名，查找任务
	 * @param flowId
	 * @param taskName
	 * @return
	 */
	public Task getTask(String flowId, String taskName);
	
	/**
	 * 删除流程
	 * @param flowId
	 */
	public void deleteTasks(String flowId);
	
	
	public void updateTaskAppend(TaskAppend taskAppend);
	
	
}
