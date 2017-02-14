package cmsz.autoflow.engine;

/**
 * 任务触发器接口
 * 任务节点配置触发器类，当到达任务节点时，自动执行该类的handle方法。
 * @author zhoushuang
 */
public interface Launcher {
	
	/**
	 * 任务trigger参数为实现Trigger接口的类， 当执行到该任务时，自动执行handle方法
	 * 业务类实现该接口
	 * @param task
	 */
	public String launch(DelegateTask dtask);
	
}
