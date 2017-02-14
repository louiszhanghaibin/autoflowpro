package cmsz.autoflow.engine;

import java.io.InputStream;

import cmsz.autoflow.engine.entity.Process;

public interface IProcessService {
	
	/**
	 * 保存流程定义对象
	 * @param process
	 */
	void saveProcess(Process process);
	
	/**
	 * 通过id获取Process对象, 并解析xml文件,设置ProcessModel对象.
	 * @param id
	 * @return
	 */
	Process getProcessById(String processId);
	
	/**
	 * 根據InputStream輸入流，部署流程定义
	 * @param input
	 * @return
	 */
	String deploy(InputStream input);
}
