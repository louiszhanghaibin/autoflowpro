package cmsz.autoflow.engine.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cmsz.autoflow.engine.AutoEngine;
import cmsz.autoflow.engine.helper.StreamHelper;
import cmsz.autoflow.engine.model.ProcessModel;
import cmsz.autoflow.engine.parser.ModelParser;
import cmsz.autoflow.engine.entity.Process;

public class ProcessXMLFileLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessXMLFileLoader.class);
	
	@Autowired
	private AutoEngine engine;
	
	private List<String> files;

	private boolean override = false;

	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	public void load() throws IOException {
		for (String name : files) {
			if (StringUtils.isEmpty(name) || StringUtils.isBlank(name)) {
				continue;
			}

			// 判断流程是否已经存在，如果存在，并且override属性为false, 则跳过，不覆盖数据库中的记录。
			boolean isExist = this.isProcessExist(name);
			if (override == false && isExist == true) {
				logger.debug("the process with file name {} has exist in db, don't override the record", name);
				continue;
			}

			InputStream in = this.getInputStream(name);
			if (in == null) {
				logger.error("the file {} resource is not found", name);
				throw new IOException("the file " + name + " resouce is not found");
			}

			String s = this.engine.process().deploy(in);
			in.close();
			if (s == null) {
				logger.error("deploy process failed with the file {}", name);
				throw new IOException("deploy process failed with the file " + name);
			}
			logger.info("the process with id {} deployed with file {}, SUCCESS", s, name);

		}
	}

	private InputStream getInputStream(String fname) {
		return this.getClass().getResourceAsStream("/" + fname);
	}

	public AutoEngine getEngine() {
		return engine;
	}

	public void setEngine(AutoEngine engine) {
		this.engine = engine;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	private boolean isProcessExist(String file) throws IOException {
		InputStream in = this.getInputStream(file);
		byte[] bytes;
		bytes = StreamHelper.readBytes(in);
		in.close();
		ProcessModel processModel = ModelParser.parse(bytes);
		String processId = processModel.getId();
		Process process = this.engine.process().getProcessById(processId);
		if (process != null)
			return true;
		return false;
	}
}
