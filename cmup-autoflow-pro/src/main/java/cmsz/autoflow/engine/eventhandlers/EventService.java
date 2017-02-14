package cmsz.autoflow.engine.eventhandlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.helper.ClassHelper;

public class EventService implements IEventService {

	private static Logger logger = LoggerFactory.getLogger(EventService.class);

	private IEventHandler eventhandler = new InnerHandler();

	List<IEventHandler> hlist = new ArrayList<IEventHandler>();

	@Override
	public void register(Class<? extends IEventHandler> clazz) {
		IEventHandler h = ClassHelper.instantiate(clazz);
		if (h != null)
			hlist.add(h);
		else
			logger.error("the class {} can not instantiate.", clazz);
	}

	@Override
	public IEventHandler getHandler() {
		return eventhandler;
	}

	class InnerHandler implements IEventHandler {
		@Override
		public void handle(Execution execution, Event event) {
			for (IEventHandler h : hlist) {
				h.handle(execution, event);
			}
		}
	}

}
