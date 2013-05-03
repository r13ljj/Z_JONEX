package filter.loophole;

import filter.loophole.config.Configuration;
import filter.loophole.config.Interceptor;
import filter.loophole.tools.TokenProcessor;
import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class InterceptorFilter
  implements Filter
{
  private String configLocation;
  private Configuration configuration;
  private FilterConfig filterConfig;
  private StringBuffer info;
  private static Logger logger = Logger.getLogger(InterceptorFilter.class);

  public void destroy()
  {
  }

  private boolean isEqualsURI(String contextPath, String curi, String ruri)
  {
    if ((curi == null) || (ruri == null)) {
      return false;
    }
    String uri = contextPath + curi;
    if (uri.equals(ruri)) {
      return true;
    }
    return false;
  }

  private boolean isValidMethod(String method, Interceptor itcpt)
  {
    List methods = itcpt.getMethods();
    for (String s : methods) {
      if (s.toUpperCase().equals(method)) {
        return true;
      }
    }
    return false;
  }

  private boolean isValidQueryString(String queryString, Interceptor itcpt)
  {
    List queryStrings = itcpt.getQueryStrings();
    if (queryStrings.size() == 0) {
      return true;
    }
    for (String s : queryStrings) {
      if (s.indexOf(queryString) >= 0) {
        return true;
      }
    }
    return false;
  }

  private boolean isValidRequest(HttpServletRequest request, HttpServletResponse response)
  {
    String uri = request.getRequestURI();
    String method = request.getMethod();
    String queryString = request.getQueryString();
    List configs = this.configuration.getConfigs();
    for (int i = 0; i < configs.size(); i++) {
      Interceptor itcpt = (Interceptor)configs.get(i);
      if ((isEqualsURI(request.getContextPath(), itcpt.getUri(), uri)) && 
        (isValidMethod(method, itcpt)) && (isValidQueryString(queryString, itcpt))) {
        return TokenProcessor.getInstance().isTokenValid(request, this.configuration.getTokenName(), true);
      }
    }

    return true;
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    HttpServletResponse httpResponse = (HttpServletResponse)response;
    if (!isValidRequest(httpRequest, httpResponse))
    {
      this.info = new StringBuffer();
      this.info.append("\nmethod:").append(httpRequest.getMethod()).append("\n");
      this.info.append("referer:").append(httpRequest.getHeader("referer")).append("\n");
      this.info.append("url:").append(httpRequest.getRequestURL()).append("\n");
      this.info.append("queryString:").append(httpRequest.getQueryString()).append("\n");
      this.info.append("当前请求不是有效请求，已被系统安全拦截，请联系管理员处理。");
      logger.info(this.info);
      httpResponse.sendError(400);
      return;
    }
    chain.doFilter(request, response);
  }

  private void setTokenName() {
    String name = this.filterConfig.getInitParameter("tokenName");
    if (name != null)
      this.configuration.setTokenName(name);
  }

  public void init(FilterConfig config) throws ServletException
  {
    this.filterConfig = config;
    this.configLocation = this.filterConfig.getServletContext().getRealPath(config.getInitParameter("filterConfigLocation"));
    this.configuration = Configuration.getInstance();
    setTokenName();
    try {
      this.configuration.init(this.configLocation);
    } catch (Exception e) {
      logger.error("漏洞过滤配置文件初始化失败。", e);
    }
  }
}
