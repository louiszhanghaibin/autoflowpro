package cmsz.autoflow.engine.access.mybatis;

import cmsz.autoflow.engine.entity.Process;

public interface ProcessMapper {

	public Process findProcessById(String Id);
	
	public void insertProcess(Process process);
	
	public void deleteProcessById(String id);
	
}
