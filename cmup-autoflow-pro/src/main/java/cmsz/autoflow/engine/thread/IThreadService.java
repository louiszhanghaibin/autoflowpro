package cmsz.autoflow.engine.thread;

import java.util.List;

import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.entity.Task;

public interface IThreadService {

	void runTask(Task task, Execution execution);
	void runTasks(List<Task> tasks, Execution execution);
}
