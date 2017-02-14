package cmsz.autoflow.engine.entity;

import java.io.Serializable;
import cmsz.autoflow.engine.model.ProcessModel;

/**
 * 流程定义实体
 * @author zhoushuang
 * 
 */
public class Process implements Serializable {
	
	private static final long serialVersionUID = 3866645078502421914L;
	/**
	 * 流程定义状态
	 */
	public static final Integer STATE_AVAIABLE =  0;
	public static final Integer STATE_DISABLE  = -1;
	
	/**
	 * 主键ID
	 */

	private String id;
	
	
	/**
	 * 名称
	 */
	private String name;
	
	
	/**
	 * 状态，是否可用
	 */
	private Integer state;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 最后更新時間
	 */
	private String updateTime;
	
	/**
	 * 流程定义xml
	 */
	private byte[] content;
	
	
	/**
	 * 流程定义模型
	 */
	private ProcessModel model;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}



	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String lastUpdateTime) {
		this.updateTime = lastUpdateTime;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] bytes) {
		this.content = bytes;
	}

	public ProcessModel getModel() {
		return model;
	}
	
	public void setModel(ProcessModel processModel) {
		this.model = processModel;
		this.name = processModel.getName();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Process(id=").append(this.id);
		sb.append(",name=").append(this.name);
		sb.append(",state=").append(this.state);
		sb.append(",createTime=").append(this.createTime);
		sb.append(",updateTime=").append(this.updateTime);
		sb.append(",content=").append(this.content);
		sb.append(",model=").append(this.model);
		return sb.toString();
	}

}
