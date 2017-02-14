package cmsz.autoflow.engine.core;

import cmsz.autoflow.engine.DBAccess;

/**
 * 抽像AccessService类，提供给子类使用access方法
 * @author zhoushuang
 */
public abstract class AccessService {
	
	protected DBAccess access;
	
	
	/**
	 * 获取数据库接口对象
	 * @return
	 */
	public DBAccess access()
	{
		return access;
	}
	
	/**
	 * 设置数据库接口对象
	 * @param access
	 */
	public void setAccess(DBAccess access)
	{
		this.access=access;
	}
	
	
}
