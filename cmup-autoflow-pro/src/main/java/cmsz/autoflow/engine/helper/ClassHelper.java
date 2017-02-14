package cmsz.autoflow.engine.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassHelper.class);
	
	public static <T> T instantiate(Class<T> clazz) {
		if (clazz.isInterface()) {
			logger.error("所传递的class类型参数为接口，无法实例化");
			return null;
		}
		try {
			return clazz.newInstance();
		} catch (Exception ex) {
			logger.error("检查传递的class类型参数是否为抽象类", ex);
		}
		return null;
	}

}
