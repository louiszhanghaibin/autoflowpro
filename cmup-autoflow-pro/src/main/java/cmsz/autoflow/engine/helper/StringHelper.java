package cmsz.autoflow.engine.helper;

public class StringHelper {

	/**
	 * 获取uuid类型的字符串
	 * @return uuid字符串
	 */
	public static String getPrimaryKey() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
	
	
	/**
	 * 判断字符串是否为空
	 * @param str 字符串
	 * @return 是否为空标识
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	/**
	 * 判断字符串是否为非空
	 * @param str 字符串
	 * @return 是否为非空标识
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
}
