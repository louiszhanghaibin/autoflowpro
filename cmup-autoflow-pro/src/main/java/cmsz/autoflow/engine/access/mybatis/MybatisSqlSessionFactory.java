package cmsz.autoflow.engine.access.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MybatisSqlSessionFactory {

  private static SqlSessionFactory sqlSessionFactory = null;
  
  private static final Logger logger = LoggerFactory.getLogger(MybatisSqlSessionFactory.class);

  public static SqlSessionFactory getSqlSessionFactory() {
    if (sqlSessionFactory == null) {
    	logger.error("SqlSessionFactory为空!工作流异常，请使用spring容器管理数据源。");
      
    	//暂时注释掉，使用spring管理数据源
      /*InputStream inputStream = null;
      InputStream istream = null;
      try {
        inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SAXReader sax = new SAXReader();
        sax.setValidation(false);
        sax.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = sax.read(inputStream);
        Element root = doc.getRootElement();
        List<Element> els = root.elements();
        for (Element e : els) {
          if (e.getName().equals("environments")) {
            List<Element> elementLists = ((List<Element>) e.elements()).get(0).elements();
            for (Element el : elementLists) {
              if (el.getName().equals("dataSource")) {
                for (Element ele : (List<Element>) el.elements()) {
                  if ("password".equals(ele.attribute("name").getText())) {
                    ele.attribute("value").setText(
                        EncryptablePropertyPlaceholderConfigurer.decryptDataMap
                            .get("jdbc.password.datasource"));
                  }
                }
              }
            }
          }

        }
        String xmlStr = doc.asXML();
        istream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(istream);
      } catch (Exception e) {
        logger.error("解析xml error" + e);
        logger.info("解析xml error" + e);
        try {
          if (null != inputStream) {
            inputStream.close();
          }
          logger.info("解析xml失败,等待5s重试");
          Thread.sleep(5000);
          if (sqlSessionFactory == null) {
            getSqlSessionFactory();
          }
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        throw new RuntimeException(e.getCause());
      } finally {
        try {
          if (null != inputStream) {
            inputStream.close();
          }
          if (null != istream) {
            istream.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }*/
    }
    return sqlSessionFactory;
  }

  public static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
	MybatisSqlSessionFactory.sqlSessionFactory = sqlSessionFactory;
  }

  public static SqlSession openSession() {
    return getSqlSessionFactory().openSession();
  }

}
