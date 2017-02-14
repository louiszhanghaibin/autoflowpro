package cmsz.autoflow.engine.model;

import cmsz.autoflow.engine.core.Execution;

public class ForkModel extends NodeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3752213958523651016L;

	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}

}
