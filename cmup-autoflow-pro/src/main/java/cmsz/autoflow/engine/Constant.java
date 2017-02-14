package cmsz.autoflow.engine;

public interface Constant {
	
	public static final String ARGS_COMMON="Common";
	
	public static final String ARGS_R_NODE="R_Node";
	
	public static final String ARGS_R_MESSAGE="R_Message";
	
	
	// 默认 DubboLauncher的id
	public static final String DEFAULT_DUBBO_LAUNCHER_ID = "defaultDubboLauncher";
	
	// 默认sqlSessionFactory的id
	public static final String DEFAULT_SQLSESSIONFACTORY_ID = "AutoFlowSqlSessionFactory";
	
	// 流程或任务状态
	public static class State{
		public static final String SUCCESS="Success";
		public static final String FINISH="Finish";
		
		public static final String START="Start";
		public static final String RUNNING="Running";
		public static final String ACTIVE="Active";
		public static final String FAILED="Failed";
		public static final String ERROR="Error";
		public static final String EXCEPTION="Exception";
		
	}
}
