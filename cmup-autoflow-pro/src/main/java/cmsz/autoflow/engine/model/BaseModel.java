package cmsz.autoflow.engine.model;

import java.io.Serializable;

import cmsz.autoflow.engine.core.Execution;
import cmsz.autoflow.engine.handlers.IHandler;

/**
 * 模型基础类
 * @author zhoushuang
 *
 */

public class BaseModel implements Serializable {

	private static final long serialVersionUID = 4819914817122330762L;
	
	private String name;
	
	protected void fire(IHandler handler, Execution execution)
	{
		handler.handle(execution);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
