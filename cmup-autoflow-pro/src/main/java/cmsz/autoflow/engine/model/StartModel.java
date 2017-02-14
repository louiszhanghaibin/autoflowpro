package cmsz.autoflow.engine.model;

import java.util.Collections;
import java.util.List;

import cmsz.autoflow.engine.core.Execution;

public class StartModel extends NodeModel {
	
	private static final long serialVersionUID = 1515125767201074105L;
	
	@Override
	public List<TransitionModel> getInputs()
	{
		return Collections.emptyList();
	}
	
	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}

}
