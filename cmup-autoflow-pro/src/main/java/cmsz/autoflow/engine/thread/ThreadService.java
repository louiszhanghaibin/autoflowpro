package cmsz.autoflow.engine.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.Constant;
import cmsz.autoflow.engine.Launcher;
import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.core.ServiceContext;
import cmsz.autoflow.engine.entity.Task;
import cmsz.autoflow.engine.helper.ClassHelper;
import cmsz.autoflow.engine.helper.StringHelper;

public class ThreadService implements IThreadService {
	private static Logger logger = LoggerFactory.getLogger(ThreadService.class);
	private ExecutorService executor = Executors.newCachedThreadPool();

	class TaskWrap implements Runnable {
		private Task task;
		private Execution execution;

		TaskWrap(Task task, Execution execution) {
			this.task = task;
			this.execution = execution;
		}

		// 执行Ref-bean id指定的bean的launcher方法
		private void executeRefBean() {
			String refbean = task.getTaskModel().getRefBean();
			Launcher launcher = ServiceContext.findByName(refbean, Launcher.class);
			String ret = launcher.launch(task);// 完成了该节点的主要功能
			logger.info("task return state {}, {}", ret, task);
			execution.getEngine().complete(task.getId(), ret, task.getUpdateVariables());
		}

		// 根据 ref-class 找到bean， 并执行launcher方法
		private void executeRefClass() throws ClassNotFoundException {
			String refclass = task.getTaskModel().getRefClass();
			Class clazz = Class.forName(refclass);
			Launcher launcher = (Launcher) ServiceContext.find(clazz);
			String ret = launcher.launch(task);// 完成了该节点的主要功能
			logger.info("task return state {}, {}", ret, task);
			execution.getEngine().complete(task.getId(), ret, task.getUpdateVariables());
		}

		// 实例化launcher类，并执行launcher方法
		private void executeLauncher() throws ClassNotFoundException {
			String classname = task.getTaskModel().getLauncher();
			Class clazz = Class.forName(classname);
			Launcher launcher = (Launcher) ClassHelper.instantiate(clazz);
			String ret = launcher.launch(task);// 完成了该节点的主要功能
			logger.info("task return state {}, {}", ret, task);
			execution.getEngine().complete(task.getId(), ret, task.getUpdateVariables());
		}

		private void executeRefDubbo() {
			// 获得任务任务初始时的参数map，此时未被任务仲参数覆盖
			Map<String, Object> oldVmap = new HashMap<>();
			oldVmap.putAll(execution.getArgs());
			String dubboId = task.getTaskModel().getRefDubbo();
			Launcher launcher = ServiceContext.findByName(Constant.DEFAULT_DUBBO_LAUNCHER_ID, Launcher.class);
			String ret = launcher.launch(task);
			logger.info("task return state {}, {}", ret, task);
			Map<String, Object> taskMap = new HashMap<>();
			taskMap.putAll(task.getVariableMap());
			taskMap.remove("Common");
			oldVmap.putAll(taskMap);
			execution.getEngine().complete(task.getId(), ret, oldVmap);
		}

		@Override
		public void run() {
			try {
				if (StringHelper.isNotEmpty(task.getTaskModel().getRefBean())) {
					executeRefBean();

				} else if (StringHelper.isNotEmpty(task.getTaskModel().getRefClass())) {

					executeRefClass();

				} else if (StringHelper.isNotEmpty(task.getTaskModel().getLauncher())) {

					executeLauncher();

				} else if (StringHelper.isNotEmpty(task.getTaskModel().getRefDubbo())) {
					executeRefDubbo();
				} else {
					logger.warn("task {} 's  ref-bean, ref-class and launcher is blank, the task is passed ",
							task.getId());
					execution.getEngine().complete(task.getId(), Constant.State.SUCCESS, task.getUpdateVariables());
				}
			} catch (Exception e) {
				logger.error("task:{} execute launcher , error hanppend, {} ", task, e.getMessage());
				execution.getEngine().complete(task.getId(), Constant.State.EXCEPTION);
			}
		}
	}// end of TaskWrap class

	@Override
	public void runTask(Task task, Execution execution) {
		TaskWrap taskwrap = new TaskWrap(task, execution);
		executor.execute(taskwrap);// 本质上是执行了taskwrap.run()
	}

	@Override
	public void runTasks(List<Task> tasks, Execution execution) {
		for (Task t : tasks) {
			runTask(t, execution);// 挨个执行executor.execute(taskwrap)
		}
	}

}
