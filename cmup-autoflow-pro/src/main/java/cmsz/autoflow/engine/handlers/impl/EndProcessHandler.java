package cmsz.autoflow.engine.handlers.impl;

import cmsz.autoflow.engine.AutoEngine;
import cmsz.autoflow.engine.Constant;
import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.handlers.IHandler;

public class EndProcessHandler implements IHandler {

	@Override
	public void handle(Execution execution) {
		Flow flow=execution.getFlow();
		AutoEngine engine=execution.getEngine();
		engine.flow().update(flow.getId(), Constant.State.FINISH);
	}

}
