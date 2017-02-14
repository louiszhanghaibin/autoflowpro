package cmsz.autoflow.engine;

import java.util.List;

/**
 * 服务查找接口。用于实现服务配置化
 * @author zhoushuang
 *
 */
public interface Context {
	
	/**
	 * 根据服务名称、实例向服务工厂注册
	 * @param name
	 * @param obj
	 */
	void put(String name, Object obj);
	
	/**
	 * 根据服务名称、类型向服务工厂注册
	 * @param name
	 * @param clazz
	 */
	 void put(String name, Class<?> clazz);
	
	/**
	 * 根据名字判断 服务是否存在
	 * @param name
	 * @return
	 */
	boolean exist(String name);
	/**
	 * 根据类型查找服务实例
	 * @param clazz
	 * @return
	 */
	<T> T find(Class<T> clazz);
	
	<T> List<T> findList(Class<T> clazz);
	
	/**
	 * 根据给定的服务名称、类型查找服务实例
	 * @param name
	 * @param clazz
	 * @return
	 */
	
	<T> T findByName(String name, Class<T> clazz);

}
