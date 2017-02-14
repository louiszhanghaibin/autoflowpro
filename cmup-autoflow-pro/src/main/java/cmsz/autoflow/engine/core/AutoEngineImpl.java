package cmsz.autoflow.engine.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.AutoEngine;
import cmsz.autoflow.engine.Constant;
import cmsz.autoflow.engine.DBAccess;
import cmsz.autoflow.engine.IFlowService;
import cmsz.autoflow.engine.IPlanService;
import cmsz.autoflow.engine.IProcessService;
import cmsz.autoflow.engine.ITaskService;
import cmsz.autoflow.engine.cfg.Configuration;
import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.entity.Process;
import cmsz.autoflow.engine.entity.Task;
import cmsz.autoflow.engine.eventhandlers.Event;
import cmsz.autoflow.engine.eventhandlers.IEventService;
import cmsz.autoflow.engine.helper.DateHelper;
import cmsz.autoflow.engine.model.NodeModel;
import cmsz.autoflow.engine.model.ProcessModel;
import cmsz.autoflow.engine.model.StartModel;
import cmsz.autoflow.engine.model.TaskModel;
import cmsz.autoflow.engine.thread.IThreadService;

public class AutoEngineImpl implements AutoEngine {

	private static final Logger logger = LoggerFactory.getLogger(AutoEngineImpl.class);

	private Configuration config;
	private IProcessService processService;
	private IFlowService flowService;
	private ITaskService taskService;

	private IPlanService planService;
	private IThreadService threadService;
	private IEventService eventService;

	@Override
	public AutoEngine configure(Configuration cfg) {

		logger.debug("configure");
		this.config = cfg;
		processService = ServiceContext.find(IProcessService.class);
		flowService = ServiceContext.find(IFlowService.class);
		taskService = ServiceContext.find(ITaskService.class);

		planService = ServiceContext.find(IPlanService.class);
		threadService = ServiceContext.find(IThreadService.class);
		eventService = ServiceContext.find(IEventService.class);

		DBAccess access = ServiceContext.find(DBAccess.class);
		setDBAccess(access);
		return this;
	}

	protected void setDBAccess(DBAccess access) {

		logger.debug("setDBAccess");

		List<AccessService> services = ServiceContext.findList(AccessService.class);
		for (AccessService as : services) {
			as.setAccess(access);
		}
	}

	@Override
	public IProcessService process() {
		return this.processService;
	}

	@Override
	public Flow startInstanceById(String id, Map<String, Object> args) {
		return startInstanceById(id, args, null, null);
	}

	@Override
	public Flow startInstanceById(String id, Map<String, Object> args, String flowId, String flowName) {
		if (args == null)
			args = new HashMap<String, Object>();
		Process process = process().getProcessById(id);
		if (process == null) {
			logger.info("process {} not exist", id);
			return null;
		}
		return startProcess(process, args, flowId, flowName);
	}

	private Flow startProcess(Process process, Map<String, Object> args, String flowId, String flowName) {

		// 启动流程前，判断是否已经有流程在执行或已执行成功，如果有，则不执行。
		List<Flow> list = this.flow().getFlow(flowName, (String) args.get("settleDate"));
		if (list != null) {
			for (Flow f : list) {
				if (f.getState() == Constant.State.ACTIVE || f.getState() == Constant.State.FINISH) {
					logger.warn("the flow instance {} {}  is active or finished, starting process is stoped", flowName,
							(String) args.get("settleDate"));
					return null;
				}
			}
		}

		Execution execution = execute(process, args, flowId, flowName);
		if (process.getModel() != null) {
			StartModel start = process.getModel().getStart();
			start.execute(execution);
		}

		// 处理流程事件
		// List<IEventHandler> handlers =
		// ServiceContext.findList(IEventHandler.class);
		// for (IEventHandler h : handlers) {
		// h.handle(execution, Event.FLOW_STARTED);
		// h.handle(execution, Event.TASK_STARTED);
		// }
		event().getHandler().handle(execution, Event.FLOW_STARTED);
		event().getHandler().handle(execution, Event.TASK_STARTED);

		// IThreadService threadpool =
		// ServiceContext.find(IThreadService.class);
		// threadpool.runTasks(execution.getTasks(), execution);

		thread().runTasks(execution.getTasks(), execution);

		return execution.getFlow();
	}

	private Execution execute(Process process, Map<String, Object> args, String flowId, String flowName) {
		Flow flow = flow().createFlow(process, args, flowId, flowName);
		Execution execution = new Execution(this, process, flow, args);
		return execution;
	}

	private Flow startProcess(Process process, Map<String, Object> args) {
		return startProcess(process, args, null, null);
	}

	private Execution execute(Process process, Map<String, Object> args) {
		return execute(process, args, null, null);
	}

	@Override
	public IFlowService flow() {
		return this.flowService;
	}

	@Override
	public ITaskService task() {
		return this.taskService;
	}

	@Override
	synchronized public Flow redoFlow(String id) {
		Flow flow = flow().getFlow(id);
		// flow.getState().equals(Flow.STATE_FAILED)
		if (flow == null) {
			logger.error("the flow {} is not exist ", id);
			return null;
		}

		if (Constant.State.ACTIVE.equals(flow.getState())) {
			logger.warn("the flow state is active, redoFlow can't executed, {} ", flow);
			return null;
		}

		task().deleteTasks(id);
		flow().update(id, Constant.State.ACTIVE);
		flow = flow().getFlow(id);
		Process process = process().getProcessById(flow.getProcessId());
		Execution execution = new Execution(this, process, flow, flow.getVariableMap());
		if (process.getModel() != null) {
			StartModel start = process.getModel().getStart();
			start.execute(execution);
		}

		// IThreadService threadpool =
		// ServiceContext.find(IThreadService.class);
		// threadpool.runTasks(execution.getTasks(), execution);

		thread().runTasks(execution.getTasks(), execution);

		return flow;
	}

	synchronized public Task complete(String id, String state) {
		return this.complete(id, state, null);
	}

	@Override
	synchronized public Task redoTask(String id) {
		Task task = task().getTask(id);
		// task.getState().equals(Task.STATE_FAILED)

		if (task != null) {
			String state = task.getState();
			// 如果任務状态处于 start, active, running 三中状态，则表示任务还在运行，此时返回null
			if (state == Constant.State.START || state == Constant.State.ACTIVE || state == Constant.State.RUNNING) {
				logger.warn("task's state mean that the task maybe is running, redo task can not executed. {}", task);
				return null;
			}
			Flow flow = flow().getFlow(task.getFlowId());
			Process process = process().getProcessById(flow.getProcessId());
			Execution execution = new Execution(this, process, flow, task.getVariableMap());

			task.setTaskModel((TaskModel) process.getModel().getNode(task.getName()));

			// 更新task的状态为Active
			task.setState(Constant.State.ACTIVE);
			task().updateTask(task);

			// IThreadService threadpool =
			// ServiceContext.find(IThreadService.class);
			// threadpool.runTask(task, execution);

			thread().runTask(task, execution);

		} else {
			logger.error("when rodo task, task:{} not found, or task is not failed", id);
		}
		logger.debug("redo task :{}", task);
		return task;
	}

	@Override
	synchronized public Task complete(String id, String state, Map<String, Object> args) {
		Task task = task().getTask(id);
		if (task == null) {
			logger.error("task {} is not exist", id);
			return null;
		}
		if (StringUtils.isEmpty(state) || StringUtils.isBlank(state)) {
			logger.error("compelete task, the return state {} is null or empty. {}", state, task);
			state = Constant.State.EXCEPTION;
		}

		task.setState(state);
		task.setFinishTime(DateHelper.getTime());
		task().updateTask(task);
		// logger.info("completeVariable : {}", task.getVariableMap());

		if (args != null) {
			task.updateVariables(args);
		}

		Flow flow = flow().getFlow(task.getFlowId());
		Process process = process().getProcessById(flow.getProcessId());
		Execution execution = new Execution(this, process, flow, task.getVariableMap());
		execution.setTask(task);

		// 进行事件处理， Task_COMPLETED
		// List<IEventHandler> handlers1 =
		// ServiceContext.findList(IEventHandler.class);
		// for (IEventHandler h : handlers1) {
		// h.handle(execution, Event.TASK_COMPLETED);
		// }
		event().getHandler().handle(execution, Event.TASK_COMPLETED);

		if (!Constant.State.SUCCESS.equals(state) && !Constant.State.RUNNING.equals(state)) {
			logger.error(
					"the state {} is not Constant.State.Success or Constant.state.RUNNING, the flow failed.  task:{}",
					state, task);
			flow().update(task.getFlowId(), Constant.State.FAILED);
			return task;
		}

		ProcessModel processModel = process.getModel();
		if (processModel != null) {
			NodeModel nodeModel = processModel.getNode(task.getName());
			task.setTaskModel((TaskModel) nodeModel);
			nodeModel.execute(execution);
		}

		// 处理流程事件
		// List<IEventHandler> handlers2 =
		// ServiceContext.findList(IEventHandler.class);
		// for (IEventHandler h : handlers2) {
		// h.handle(execution, Event.TASK_STARTED);
		// }
		event().getHandler().handle(execution, Event.TASK_STARTED);

		// IThreadService threadpool =
		// ServiceContext.find(IThreadService.class);
		// threadpool.runTasks(execution.getTasks(), execution);

		thread().runTasks(execution.getTasks(), execution);

		return null;
	}// complete

	@Override
	public IPlanService plan() {
		return this.planService;
	}

	@Override
	public IThreadService thread() {
		return this.threadService;
	}

	@Override
	public IEventService event() {
		return this.eventService;
	}

}
