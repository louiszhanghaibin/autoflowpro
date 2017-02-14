package cmsz.autoflow.engine.model;

import cmsz.autoflow.engine.Action;
import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.handlers.impl.CreateTaskHandler;

public class TransitionModel extends BaseModel implements Action{

	private static final long serialVersionUID = 1302144218510794007L;
	
	private boolean enabled = false;
	
	private NodeModel source;
	
	private NodeModel target;
	
	private String to;
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public NodeModel getTarget() {
		return target;
	}

	public void setTarget(NodeModel target) {
		this.target = target;
	}

	public void setEnabled(boolean bool) {
		this.enabled=bool;
	}

	@Override
	public void execute(Execution execution) {
		if(!enabled) return;
		if(target instanceof TaskModel)
		{
			CreateTaskHandler handler=new CreateTaskHandler((TaskModel)target);
			fire(handler, execution);
		}else
		{
			target.execute(execution);
		}
	}

	public NodeModel getSource() {
		return source;
	}

	public void setSource(NodeModel source) {
		this.source = source;
	}


	





}
