package cmsz.autoflow.engine.cfg;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.AutoEngine;
import cmsz.autoflow.engine.Constant;
import cmsz.autoflow.engine.Context;
import cmsz.autoflow.engine.access.MyBatisAccess;
import cmsz.autoflow.engine.access.mybatis.MybatisSqlSessionFactory;
import cmsz.autoflow.engine.core.AutoEngineImpl;
import cmsz.autoflow.engine.core.FlowService;
import cmsz.autoflow.engine.core.PlanService;
import cmsz.autoflow.engine.core.ProcessService;
import cmsz.autoflow.engine.core.ServiceContext;
import cmsz.autoflow.engine.core.TaskService;
import cmsz.autoflow.engine.eventhandlers.EventService;
import cmsz.autoflow.engine.eventhandlers.IEventService;
import cmsz.autoflow.engine.eventhandlers.impl.BusiDateAppendHandler;
import cmsz.autoflow.engine.impl.SimpleContext;
import cmsz.autoflow.engine.thread.ThreadService;

/**
 * 配置类
 * 
 * @author zhoushuang
 * 
 */
public class Configuration {

	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

	public Configuration() {
		this(new SimpleContext());
	}

	public Configuration(Context context) {
		ServiceContext.setContext(context);
	}

	/**
	 * 实例化一个流程引擎
	 * 
	 * @return
	 */
	public AutoEngine buildAutoEngine() {
		logger.debug("buildQuickEngine : 将各种实现类加载到ServiceContext中");
		// 配置ServiceContext中的Service
		// 最终是在SimpleContext.contextMap属性中加入了name,
		// ClassHelper.instantiate(clazz)
		// 即name和该类的实体
		ServiceContext.put("cmsz.quickflow.engine.IFlowService", FlowService.class);
		ServiceContext.put("cmsz.quickflow.engine.IProcessService", ProcessService.class);
		ServiceContext.put("cmsz.quickflow.engine.ITaskService", TaskService.class);
		ServiceContext.put("cmsz.quickflow.engine.DBAccess", MyBatisAccess.class);
		ServiceContext.put("cmsz.quickflow.engine.core.AutoEngine", AutoEngineImpl.class);
		ServiceContext.put("cmsz.autoflow.engine.thread.ThreadService", ThreadService.class);
		ServiceContext.put("cmsz.autoflow.engine.core.PlanService", PlanService.class);
		
		// Event处理相关配置
//		ServiceContext.put("cmsz.autoflow.engine.eventhandlers.impl.BusiDateAppendHandler", BusiDateAppendHandler.class); // 增加   // EventHanlder
		ServiceContext.put("cmsz.autoflow.engine.eventhandlers.EventService", EventService.class);																													// 业务处理的
		IEventService eventservice=ServiceContext.find(IEventService.class);
		eventservice.register(BusiDateAppendHandler.class);
		
		
		
		// 增加spring配置datasource 与 sqlSessionFacoty
		SqlSessionFactory sqlSessionFactory = ServiceContext.findByName(Constant.DEFAULT_SQLSESSIONFACTORY_ID, SqlSessionFactory.class);
		if (sqlSessionFactory != null) {
			logger.info("AutoFlow has get sqlSessionFactory from Spring context by bean id "+ Constant.DEFAULT_SQLSESSIONFACTORY_ID);
			MybatisSqlSessionFactory.setSqlSessionFactory(sqlSessionFactory);
		} else { 
			//單數據源，取Spring中默認的一個SqlSessionFactory
			sqlSessionFactory = ServiceContext.find(SqlSessionFactory.class);
			if (sqlSessionFactory != null) {
				logger.info("AutoFlow has get sqlSessionFactory from Spring context");
				MybatisSqlSessionFactory.setSqlSessionFactory(sqlSessionFactory);
			}
		}

		AutoEngine configEngine = ServiceContext.getEngine();
		return configEngine.configure(this);// 对configEngine的属性进行实例化
	}

}
