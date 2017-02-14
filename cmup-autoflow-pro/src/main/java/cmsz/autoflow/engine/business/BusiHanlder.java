package cmsz.autoflow.engine.business;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.access.mybatis.MybatisSqlSessionFactory;
import cmsz.autoflow.engine.entity.Flow;
import cmsz.autoflow.engine.entity.Task;
import cmsz.autoflow.engine.helper.JsonHelper;

public class BusiHanlder implements IBusiHandler {
 
  private static final Logger logger = LoggerFactory.getLogger(BusiHanlder.class);

  private static final String DATA_KEY = "head";

  static public class BusiData {
    public String settleDate;

    public String getSettleDate() {
      return settleDate;
    }

    public void setSettleDate(String settleDate) {
      this.settleDate = settleDate;
    }

    public String busiLine;

    public String getBusiLine() {
      return busiLine;
    }

    public void setBusiLine(String busiLine) {
      this.busiLine = busiLine;
    }

    @Override
    public String toString() {
      return JsonHelper.toJson(this);
    }
  }

  public BusiData getData(String argstr) {
    Map<String, Object> map = JsonHelper.fromJson(argstr, Map.class);
    Map<String, Object> headmap = (Map<String, Object>) map.get(DATA_KEY);
    BusiData data = JsonHelper.fromJson(JsonHelper.toJson(headmap), BusiData.class);
    return data;
  }

  @Override
  public void handle(Object obj) {
    String args = null;
    String id = null;
    if (obj instanceof Flow) {
      Flow flow = (Flow) obj;
      id = flow.getId();
      args = flow.getVariables();
    } else if (obj instanceof Task) {
      Task task = (Task) obj;
      id = task.getId();
      args = task.getVariables();
    } else {

      String msgContext = "BusiHanlder can't handle object:"+obj.toString();
      logger.error(msgContext);
      return;
    }
    SqlSession sqlSession = MybatisSqlSessionFactory.openSession();


  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    BusiHanlder handle = new BusiHanlder();
    String json = "{\"head\":{\"settleDate\":\"xxdd\", \"busiLine\":\"tmall\"}, \"body\": null }";
    BusiData data = handle.getData(json);
    System.out.println("data=" + data);

    Map<String, Object> map = JsonHelper.fromJson(json, Map.class);
    Map<String, Object> map2 = (Map<String, Object>) map.get("head");
    System.out.println(JsonHelper.toJson(map2));

  }

}
