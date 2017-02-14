package cmsz.autoflow.engine.handlers.impl;

import cmsz.autoflow.engine.AutoEngine;
import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.entity.Task;
import cmsz.autoflow.engine.handlers.IHandler;
import cmsz.autoflow.engine.helper.JsonHelper;
import cmsz.autoflow.engine.model.TaskModel;

public class CreateTaskHandler implements IHandler {

	private TaskModel model;

	private Task task;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public CreateTaskHandler(TaskModel target) {
		this.model = target;
	}

	@Override
	public void handle(Execution execution) {
		AutoEngine engine = execution.getEngine();
		task = engine.task().getTask(execution.getFlow().getId(),
				model.getName());
		if (task != null)
		{
			task.setTaskModel(model);
			task.setVariables(JsonHelper.toJson(execution.getArgs()));
			engine.task().updateTask(task);
		}
		else
			task = engine.task().createTask(model, execution);
		execution.addTask(task);
	}

}
