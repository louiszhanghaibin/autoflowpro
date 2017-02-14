package cmsz.autoflow.engine.model;

import java.util.ArrayList;
import java.util.List;

import cmsz.autoflow.engine.Action;
import cmsz.autoflow.engine.core.Execution;

/**
 * 节点元素 有输入、输出
 * 
 * @author zhoushuang
 * 
 */

public abstract class NodeModel extends BaseModel implements Action {

	private static final long serialVersionUID = -2548092910536984853L;

	private List<TransitionModel> inputs = new ArrayList<TransitionModel>();
	private List<TransitionModel> outputs = new ArrayList<TransitionModel>();
	private List<FieldModel> fieldList;

	private String refBean;
	private String refClass;

	// refDubbo 与 refComponent 同时使用
	private String refDubbo;
	private String refComponent;

	public String getRefComponent() {
		return refComponent;
	}

	public void setRefComponent(String refComponent) {
		this.refComponent = refComponent;
	}

	public String getRefClass() {
		return refClass;
	}

	public void setRefClass(String refClass) {
		this.refClass = refClass;
	}

	public String getRefDubbo() {
		return refDubbo;
	}

	public void setRefDubbo(String refDubbo) {
		this.refDubbo = refDubbo;
	}

	public List<FieldModel> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<FieldModel> fieldList) {
		this.fieldList = fieldList;
	}

	/**
	 * 配置
	 */
	private String launcher;

	public String getLauncher() {
		return launcher;
	}

	public void setLauncher(String launcher) {
		this.launcher = launcher;
	}

	protected abstract void exec(Execution execution);

	public void execute(Execution execution) {
		exec(execution);
	}

	public void setOutputs(List<TransitionModel> outputs) {
		this.outputs = outputs;
	}

	public List<TransitionModel> getOutputs() {
		return outputs;
	}

	public void setInputs(List<TransitionModel> inputs) {
		this.inputs = inputs;

	}

	public List<TransitionModel> getInputs() {
		return inputs;
	}

	/**
	 * 运行变迁
	 * 
	 * @param execution
	 */

	protected void runOutTransition(Execution execution) {
		for (TransitionModel tm : getOutputs()) {
			tm.setEnabled(true);
			tm.execute(execution);
		}
	}

	public <T> List<T> getNextModels(Class<T> clazz) {
		List<T> models = new ArrayList<T>();
		for (TransitionModel tm : this.getOutputs()) {
			addNextModels(models, tm, clazz);
		}
		return models;
	}

	@SuppressWarnings("unchecked")
	protected <T> void addNextModels(List<T> models, TransitionModel tm, Class<T> clazz) {
		if (clazz.isInstance(tm.getTarget())) {
			models.add((T) tm.getTarget());
		} else {
			for (TransitionModel tm2 : tm.getTarget().getOutputs()) {
				addNextModels(models, tm2, clazz);
			}
		}

	}

	public String getRefBean() {
		return refBean;
	}

	public void setRefBean(String refBean) {
		this.refBean = refBean;
	}

}
