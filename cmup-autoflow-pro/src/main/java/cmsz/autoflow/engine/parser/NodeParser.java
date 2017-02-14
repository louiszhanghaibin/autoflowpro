package cmsz.autoflow.engine.parser;

public interface NodeParser {

	public static final String ATTR_NAME = "name";
	public static final String ATTR_ID = "id";
	public static final String ATTR_TO = "to";
	public static final String ATTR_LAUNCHER = "launcher";
	public static final String ATTR_REFBEAN = "refbean";
	public static final String ATTR_REFCLASS = "refclass";
	
	public static final String ATTR_KEY = "key";
	public static final String ATTR_VALUE = "value";
	
	// 使用dubbo remote调用时使用
	public static final String ATTR_REFDUBBO = "refdubbo";
	public static final String ATTR_COMPONENT = "refcomponent";
	
	public static final String ELEM_START="start";
	public static final String ELEM_TASK="task";
	public static final String ELEM_END="end";
	public static final String ELEM_TRAN="transition";
	public static final String ELEM_FORK="fork";
	public static final String ELEM_JOIN="join";
	public static final String ELEM_FIELD="field";
	
}
