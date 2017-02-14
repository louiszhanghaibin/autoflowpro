package cmsz.autoflow.engine.eventhandlers;

import cmsz.autoflow.engine.core.Execution;

public interface IEventHandler {
	void handle(Execution execution, Event event);
}
