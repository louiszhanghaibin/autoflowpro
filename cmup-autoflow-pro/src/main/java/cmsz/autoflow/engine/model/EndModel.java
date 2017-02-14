package cmsz.autoflow.engine.model;

import java.util.Collections;
import java.util.List;

import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.handlers.impl.EndProcessHandler;

public class EndModel extends NodeModel {

	private static final long serialVersionUID = 2666225639860732310L;

	
	public List<TransitionModel> getOutputs()
	{
		return Collections.emptyList();
	}
	
	@Override
	protected void exec(Execution execution) {
		fire(new EndProcessHandler(), execution);
	}

}
