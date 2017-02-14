package cmsz.autoflow.engine.cfg;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cmsz.autoflow.engine.core.ServiceContext;
import cmsz.autoflow.engine.impl.SupportSpringContext;

public class SupportSpringConfiguration extends Configuration implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	private SupportSpringContext context;
	
	public SupportSpringConfiguration()
	{
		super(null);
		this.context = new SupportSpringContext();//空构造函数
		ServiceContext.setContext(this.context);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		
		this.context.setApplicationContext(arg0);
	}

}
