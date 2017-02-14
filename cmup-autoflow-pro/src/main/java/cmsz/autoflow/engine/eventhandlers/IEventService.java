package cmsz.autoflow.engine.eventhandlers;

public interface IEventService {
	
	void register(Class<? extends IEventHandler> clazz);
	IEventHandler getHandler();
	
}