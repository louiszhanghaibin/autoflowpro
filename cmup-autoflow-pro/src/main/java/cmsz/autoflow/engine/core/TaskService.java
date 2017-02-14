package cmsz.autoflow.engine.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmsz.autoflow.engine.Constant;
import cmsz.autoflow.engine.ITaskService;
import cmsz.autoflow.engine.entity.Task;
import cmsz.autoflow.engine.entity.TaskAppend;
import cmsz.autoflow.engine.helper.DateHelper;
import cmsz.autoflow.engine.helper.JsonHelper;
import cmsz.autoflow.engine.helper.SequenceHelper;
import cmsz.autoflow.engine.model.FieldModel;
import cmsz.autoflow.engine.model.TaskModel;

public class TaskService extends AccessService implements ITaskService {

	@Override
	public void saveTask(Task task) {
		access().saveTask(task);
	}

	@Override
	public Task createTask(TaskModel model, Execution execution) {
		Task task = new Task();
		task.setId("TASK-" + DateHelper.getDate(0) + "-" + SequenceHelper.getSequence());
		task.setName(model.getName());

		task.setFlowId(execution.getFlow().getId());
		task.setFlowName(execution.getFlow().getName());

		task.setProcessId(execution.getProcess().getId());
		task.setProcessName(execution.getProcess().getName());
		task.setCreateTime(DateHelper.getTime());
		task.setUpdateTime(task.getCreateTime());

		task.setTaskModel(model);

		Map<String, Object> varMap = new HashMap<>();
		varMap.putAll(execution.getArgs());
		Map<String, String> comMap = new HashMap<>();
		comMap.putAll((Map<String, String>) varMap.get("Common"));
		varMap.remove("Common");
		List<FieldModel> fieldList = model.getFieldList();
		for (FieldModel fieldModel : fieldList) {
			String key = fieldModel.getKey();
			String value = fieldModel.getValue();
			comMap.put(key, value);
		}
		varMap.put("Common", comMap);
		task.setVariables(JsonHelper.toJson(varMap));
		task.setState(Constant.State.START);
		this.saveTask(task);
		return task;
	}

	@Override
	public Task getTask(String id) {
		Task task = access().getTask(id);
		return task;
	}

	@Override
	public Task getTask(String flowId, String taskName) {
		Task task = new Task();
		task.setFlowId(flowId);
		task.setName(taskName);
		return access().getTask(task);
	}

	@Override
	public void deleteTasks(String flowId) {
		access.deleteTasksByFlowId(flowId);
	}

	@Override
	public void updateTask(Task task) {
		access.updateTask(task);
	}

	@Override
	public void updateTaskAppend(TaskAppend taskAppend) {
		access().updateTaskAppend(taskAppend);
	}

}
