package cmsz.autoflow.engine.core;

import java.util.List;

import cmsz.autoflow.engine.Context;
import cmsz.autoflow.engine.AutoEngine;

/**
 * 单实例上下文接口
 * @author zhoushuang
 *
 */

public abstract class ServiceContext {
	
	/**
	 * 上下文服务接口
	 */
	private static Context context;
	
	/**
	 * 流程引擎的引用 
	 */
	private static AutoEngine engine;
	
	public static Context getContext()
	{
		return context;
	}
	
	public static void setContext(Context context)
	{
		ServiceContext.context=context;
	}
	
	public static AutoEngine getEngine()
	{
		if(engine == null)
		{
			engine = context.find(AutoEngine.class);
		}
		return engine;
	}
	
	/**
	 * 向上下文添加服务实例
	 * @param name
	 * @param obj
	 */
	
	public static void put(String name, Object obj)
	{
		context.put(name, obj);
	}
	
	/**
	 * 向上下文添加服务实例
	 * @param name
	 * @param clazz
	 */
	public static void put(String name, Class<?> clazz)
	{
		context.put(name, clazz);
	}
	
	
	/**
	 * 根据服务名称判断是否存在服务实例
	 * @param name 服务名称
	 * @return
	 */
	public static boolean exist(String name) {
		return context.exist(name);
	}
	
	/**
	 * 对外部提供的查找对象方法，根据class类型查找
	 * @param clazz 服务类型
	 * @return
	 */
	public static <T> T find(Class<T> clazz) {
		return context.find(clazz);
	}
	
	/**
	 * 对外部提供的查找对象实例列表方法，根据class类型查找集合
	 * @param clazz 服务类型
	 * @return
	 */
	public static <T> List<T> findList(Class<T> clazz) {
		return context.findList(clazz);
	}
	
	/**
	 * 对外部提供的查找对象方法，根据名称、class类型查找
	 * @param name 服务名称
	 * @param clazz 服务类型
	 * @return
	 */
	public static <T> T findByName(String name, Class<T> clazz) {
		return context.findByName(name, clazz);
	}
	
}
