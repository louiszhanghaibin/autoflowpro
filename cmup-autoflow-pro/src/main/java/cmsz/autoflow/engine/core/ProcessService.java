package cmsz.autoflow.engine.core;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.IProcessService;
import cmsz.autoflow.engine.entity.Process;
import cmsz.autoflow.engine.helper.DateHelper;
import cmsz.autoflow.engine.helper.StreamHelper;
import cmsz.autoflow.engine.model.ProcessModel;
import cmsz.autoflow.engine.parser.ModelParser;

public class ProcessService extends AccessService implements IProcessService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);
	/**
	 * 保存process定义到数据库
	 * 
	 */
	@Override
	public void saveProcess(Process process) {
		// 先删除相同id的流程定义，再插入新流程
		access().deleteProcess(process);
		access().saveProcess(process);
	}

	@Override
	public Process getProcessById(String id) {
		Process process = null;
		process = access().getProcess(id);
		if (process != null) {
			process.setModel(ModelParser.parse(process.getContent()));
		}
		return process;
	}


	/**
	 * 把对账xml的InputStream，解析成ProcessModel，再依据ProcessModel
	 * 生成Process实体，同时存进数据库表AF_PROCESS中，最后将该实体的ID（String）返回
	 */
	@Override
	public String deploy(InputStream input) {
		try {
			byte[] bytes;
			bytes = StreamHelper.readBytes(input);
			ProcessModel processModel= ModelParser.parse(bytes);
			Process entity=new Process();
			entity.setName(processModel.getName());
			entity.setContent(bytes);
			entity.setModel(processModel);
			entity.setId(processModel.getId());
			entity.setCreateTime(DateHelper.getTime());
			entity.setUpdateTime(entity.getCreateTime());
			entity.setState(Process.STATE_AVAIABLE);
			this.saveProcess(entity);
			return entity.getId();
		} catch (IOException e) {
			logger.debug("deploy flow failed:",e);
		}
		return null;
	}
}
