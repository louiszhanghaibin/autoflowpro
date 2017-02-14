package cmsz.autoflow.engine.model;

import cmsz.autoflow.engine.core.Execution;

public class TaskModel extends NodeModel {
	
	private static final long serialVersionUID = 8453665111906546669L;

	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}

}
