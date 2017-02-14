package cmsz.autoflow.engine.server;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.core.ServiceContext;
import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.entity.Task;
import cmsz.autoflow.engine.helper.JsonHelper;
import cmsz.autoflow.engine.helper.StringHelper;

public class JsonContainer implements Container {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonContainer.class);

	public static final String CMD_STARTFLOW = "StartFlow";
	public static final String CMD_COMPLETE = "CompleteTask";
	public static final String CMD_REDOFLOW = "RedoFlow";
	public static final String CMD_REDOTASK = "RedoTask";
	
	private void service( Request req, Response resp )
	{
		try {
			String reqcontent = req.getContent();
			logger.info("receive request:"+ reqcontent);
			PrintStream body = resp.getPrintStream();
			Command cmd = JsonHelper.fromJson(reqcontent, Command.class);
			if (cmd == null) {

				logger.error("Request content is not a json string  : " + reqcontent);

				body.println(JsonHelper .toJson(new Result(1, "Request content is not a json string : " + reqcontent)));
				body.close();
				return;
			}
			
			String operate=cmd.getOperate();
			if (StringHelper.isEmpty(operate) ) {
				String msgContext = "flow or task operate Arguments is empty,flow engine don't know how to do for next, Request content is: " + reqcontent;
				logger.error(msgContext);

				body.println(JsonHelper .toJson(new Result(1, msgContext)));
				body.close();
				return;
			}
			
			if (operate.equals(CMD_STARTFLOW)) {
				Map<String, Object> args = (Map<String, Object>) cmd.getArgs();
				Flow flow = ServiceContext.getEngine().startInstanceById(cmd.getId(), args);
				if(flow != null ){
					logger.info("start flow , flow:"+flow);
					body.println(JsonHelper.toJson(new Result(0, "start flow , flow :"+flow)));
					body.close();
					return;
				}
			} else if (operate.equals(CMD_COMPLETE) ) {
				Task task = ServiceContext.getEngine().complete(cmd.getId(), cmd.getState());
				if(task != null){
					logger.info("task complete, task :"+task);
					body.println(JsonHelper.toJson(new Result(0, "complete task, task :"+ task)));
					body.close();
					return ;
				}
			} else if (operate.equals(CMD_REDOFLOW) ) {
				Flow flow = ServiceContext.getEngine().redoFlow(cmd.getId());
				if (flow != null) {
					logger.info("redo flow , flow: "+ flow);
					body.println(JsonHelper.toJson(new Result(0, "redo flow : "+ flow )));
					body.close();
					return ;
				}
			} else if (operate.equals( CMD_REDOTASK) ) {
				Task task = ServiceContext.getEngine().redoTask(cmd.getId());
				if (task != null) {
					logger.info("redo task , flow: "+task);
					body.println(JsonHelper.toJson(new Result(0, "redo task : "+ task )));
					body.close();
					return ;
				}
			} else {

				String msgContext = "Request content has errors : " + reqcontent;
				logger.error(msgContext);

				body.println(JsonHelper.toJson(new Result(1, msgContext)));
				body.close(); 
				return;
			}

			String msgContext = "operate failed : " + reqcontent;
			logger.error(msgContext);
			
			
			body.println(JsonHelper.toJson(new Result(1, msgContext)));
			body.close(); 
			return ;
		} catch (IOException e) {

			logger.error(" IOException",e);

		} 
		
		
	}
	
	
	@Override
	public void handle(Request req, Response resp) {
		logger.debug("***START "+Thread.currentThread().getName() + "  req:" + req.hashCode() + " resp:"+ resp.hashCode());
		service(req, resp);
		logger.debug("***END   "+Thread.currentThread().getName() + "  req:" + req.hashCode() + " resp:"+ resp.hashCode());
		
	}


	static public class Command {
		public String operate;
		public String id;
		public String state;
		public Object args;

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public Object getArgs() {
			return args;
		}

		public void setArgs(Object args) {
			this.args = args;
		}

		public String getOperate() {
			return operate;
		}

		public void setOperate(String operate) {
			this.operate = operate;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	static public class Arguments {
		public Object getHead() {
			return head;
		}

		public void setHead(ArgsHead head) {
			this.head = head;
		}

		public Object getBody() {
			return body;
		}

		public void setBody(Object body) {
			this.body = body;
		}

		public ArgsHead head;
		public Object body;
	}

	static public class ArgsHead {
		public String settleDate;
		public String busiLine;
		public String province;
	}

	static public class Result {
		public Result(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		private int code;
		private String msg;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

}
