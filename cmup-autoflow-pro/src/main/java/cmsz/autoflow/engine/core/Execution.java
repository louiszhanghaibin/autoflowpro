package cmsz.autoflow.engine.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import cmsz.autoflow.engine.AutoEngine;
import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.entity.Process;
import cmsz.autoflow.engine.entity.Task;

/**
 * 执行对象
 * @author zhoushuang
 *
 */
public class Execution  implements Serializable {
	
	private static final long serialVersionUID = -7555588579762393297L;
	
	/**
	 * QuickEnagin holder
	 * 
	 */
	private AutoEngine engine;
	
	/**
	 *  流程定义
	 */
	private transient Process  process;
	
	/**
	 *  流程实例
	 */
	private Flow flow;
	
	/**
	 * 执行参数
	 */
	private transient Map<String, Object> args;
	
	/**
	 * 已被执行任务
	 */
	private Task task;
	
	/** 
	 * 返回的任务列表， 需要执行的任务，已触发。
	 */
    private List<Task> tasks = new ArrayList<>();
	
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public Execution(AutoEngine engine, Process process, Flow flow, Map<String, Object> args) {	
		this.engine=engine;
		this.process=process;
		this.flow = flow;
		this.args = args;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public AutoEngine getEngine() {
		return engine;
	}

	public Process getProcess() {
		return process;
	}
	
	public Flow getFlow()
	{
		return flow;
	}

	public Map<String, Object> getArgs()
	{
		return this.args;
	}
	
	public void addTask(Task task)
	{
		this.tasks.add(task);
	}
	
}
