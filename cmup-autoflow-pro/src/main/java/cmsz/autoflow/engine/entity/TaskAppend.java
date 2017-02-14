package cmsz.autoflow.engine.entity;

public class TaskAppend {
	public String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String settleDate;
	public String busiLine;
	public String province;
	
	public String node;
	public String message;
	
	public String getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	public String getBusiLine() {
		return busiLine;
	}
	public void setBusiLine(String busiLine) {
		this.busiLine = busiLine;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getMsg() {
		return message;
	}
	public void setMsg(String msg) {
		this.message = msg;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("TaskAppend(id=").append(this.id);
		sb.append(",settleDate=").append(this.settleDate);
		sb.append(",province=").append(this.province);
		sb.append(",busiLine=").append(this.busiLine);
		sb.append(",node=").append(this.node);
		sb.append(",message=").append(this.message);
		sb.append(")");
		return sb.toString();
	}
}
