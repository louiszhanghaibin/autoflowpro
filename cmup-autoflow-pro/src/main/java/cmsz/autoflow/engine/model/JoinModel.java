package cmsz.autoflow.engine.model;

import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.handlers.impl.MergeBranchHandler;

public class JoinModel extends NodeModel {

	private static final long serialVersionUID = -4198291062411212158L;

	@Override
	protected void exec(Execution execution) {
		MergeBranchHandler handler=new MergeBranchHandler(this);
		fire(handler, execution);
		if(handler.isMerged()) runOutTransition(execution);
	}

}
