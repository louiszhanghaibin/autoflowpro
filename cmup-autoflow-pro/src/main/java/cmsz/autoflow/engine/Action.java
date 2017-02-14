package cmsz.autoflow.engine;

import cmsz.autoflow.engine.core.Execution;
/**
 * 所有的模型对象需要实现的接口，需要实现execute方法，每个节点的执行方式不一样
 * @author zhoushuang
 */

public interface Action {
	
	/**
	 * 所有模型对象通过execute方法推动流程前进
	 * @param execution
	 */
	public void execute(Execution execution);
	
}
