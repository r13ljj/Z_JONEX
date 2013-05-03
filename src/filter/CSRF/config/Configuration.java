package filter.CSRF.config;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Configuration
{
  private String tokenName;
  private List<Interceptor> configs;
  private static Configuration configuration = new Configuration();

  public static Configuration getInstance()
  {
    return configuration;
  }

  public static void main(String[] args) {
    String path = "D:/workspaces/myworkspace/.metadata/.plugins/com.genuitec.eclipse.easie.tomcat.myeclipse/tomcat/webapps/Debug/WEB-INF/filter.cfg.xml";
    Configuration c = new Configuration();
    try {
      c.init(path);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getTokenName() {
    return this.tokenName;
  }

  public void setTokenName(String tokenName) {
    this.tokenName = tokenName;
  }

  public List<Interceptor> getConfigs() {
    return this.configs;
  }

  public void init(String path) throws Exception {
    this.configs = new ArrayList();
    SAXReader reader = new SAXReader();
    Document doc = reader.read(new FileInputStream(path));
    Element root = doc.getRootElement();
    Iterator it = root.elementIterator();

    while (it.hasNext()) {
      Element el = (Element)it.next();
      if (el.getName().toLowerCase().equals("interceptor"))
      {
        this.configs.add(parseInterceptor(el));
      }
    }
  }

  private Interceptor parseInterceptor(Element element) throws Exception {
    Iterator it = element.elementIterator();
    Interceptor itcpt = new Interceptor();

    while (it.hasNext()) {
      Element el = (Element)it.next();
      if (el.getName().toLowerCase().equals("uri"))
      {
        itcpt.setUri(getText4EL(el));
      } else if (el.getName().toLowerCase().equals("method"))
      {
        itcpt.addMethod(getText4EL(el));
      } else if (el.getName().toLowerCase().equals("querystring"))
      {
        itcpt.addQueryStrings(getText4EL(el));
      }
      else {
        throw new Exception("不符合规范的配置：" + el.getName());
      }
    }
    return itcpt;
  }

  private String getText4EL(Element el) {
    if (el.hasContent()) {
      return el.getText();
    }
    return "";
  }
}
