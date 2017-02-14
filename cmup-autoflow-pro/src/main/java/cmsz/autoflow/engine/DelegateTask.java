package cmsz.autoflow.engine;

import java.util.Map;

public interface DelegateTask {

	public String getId();
	
	public String getFlowId();
	
	public String getName();

	public String getCreateTime();
	
	public String getVariables();
	
	public String getState();
	
	public String getFlowName();
	
	public String getProcessName();
	
	public String getProcessId();
	
	public String getDubboId();
	
	public String getComponentId();
	
	public void updateVariables(Map<String, Object> args);
	

}
