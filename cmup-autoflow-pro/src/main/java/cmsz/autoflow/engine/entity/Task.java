package cmsz.autoflow.engine.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cmsz.autoflow.engine.DelegateTask;
import cmsz.autoflow.engine.helper.JsonHelper;
import cmsz.autoflow.engine.helper.StringHelper;
import cmsz.autoflow.engine.model.TaskModel;

public class Task implements Serializable, DelegateTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = -57576037706626126L;

	// 任务状态
	// public static final String STATE_START = "START";
	// public static final String STATE_RUNNING = "RUNNING";
	// public static final String STATE_EXCEPTION = "EXCEPTION";
	// public static final String STATE_FAILED = "FAILED";
	// public static final String STATE_SUCCESS = "SUCCESS";
	// public static final String STATE_ERROR = "ERROR";

	/**
	 * 主键 id
	 */

	private String id;

	/**
	 * 流程Id
	 */
	private String flowId;

	/**
	 * 流程实例名字
	 */
	private String flowName;

	/**
	 * 流程定义Id
	 */
	private String processId;

	/**
	 * 流程定义名
	 */
	private String processName;

	/**
	 * 任务名字
	 */
	private String name;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 完成时间
	 */
	private String finishTime;

	/**
	 * 任务模型对象
	 */
	private TaskModel taskModel;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 任务变量
	 */
	private String variables;

	public String getComponentId() {
		return taskModel.getRefComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cmsz.autoflow.engine.DelegateTask#getDubboId()
	 */
	@Override
	public String getDubboId() {
		return taskModel.getRefDubbo();
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String taskName) {
		this.name = taskName;
	}

	@Override
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public TaskModel getTaskModel() {
		return taskModel;
	}

	public void setTaskModel(TaskModel taskModel) {
		this.taskModel = taskModel;
	}

	@Override
	public String getVariables() {
		return variables;
	}

	public Map<String, Object> getVariableMap() {
		return this.variables == null ? new HashMap<String, Object>() : JsonHelper.fromJson(this.variables, Map.class);
	}

	public void setVariables(String variables) {
		this.variables = variables;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Task(id=").append(this.id);
		sb.append(",taskName=").append(this.name);
		sb.append(",flowId=").append(this.flowId);
		sb.append(",flowName=").append(this.flowName);
		sb.append(",processId=").append(this.processId);
		sb.append(",processName=").append(this.processName);
		sb.append(",state=").append(this.state);
		sb.append(",variables=").append(this.variables);
		sb.append(",taskModel=").append(this.taskModel);
		sb.append(",createTime=").append(this.createTime);
		sb.append(",updateTime=").append(this.updateTime);
		sb.append(",finishTime=").append(this.finishTime);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String lastUpdateTime) {
		this.updateTime = lastUpdateTime;
	}

	@Override
	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	@Override
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	private transient Map<String, Object> updateVariables = null;

	@Override
	public void updateVariables(Map<String, Object> args) {
		if (args == null)
			return;
		if (this.updateVariables == null) {
			this.updateVariables = new HashMap<String, Object>();
		}
		this.updateVariables.putAll(args);
		// 需要将流程参数map进行还原更新
		// args.remove(Constant.ARGS_COMMON);
		if (StringHelper.isNotEmpty(this.variables)) {
			Map<String, Object> vars = JsonHelper.fromJson(this.variables, Map.class);
			vars.putAll(args);
			this.variables = JsonHelper.toJson(vars);
		} else {
			this.variables = JsonHelper.toJson(args);
		}
	}

	public void setUpdateVariables(Map<String, Object> updateVariables) {
		this.updateVariables = updateVariables;
	}

	public Map<String, Object> getUpdateVariables() {
		return this.updateVariables;
	}

}
