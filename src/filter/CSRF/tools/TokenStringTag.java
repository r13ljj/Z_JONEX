package filter.CSRF.tools;

import filter.loophole.config.Configuration;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class TokenStringTag extends TagSupport
{
  private String uri;

  public String getUri()
  {
    return this.uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public int doEndTag() throws JspException
  {
    HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
    HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();
    String token = TokenProcessor.getInstance().generateToken(request, Configuration.getInstance().getTokenName(), request.getContextPath() + this.uri);
    JspWriter out = this.pageContext.getOut();
    try {
      out.write(Configuration.getInstance().getTokenName());
      out.write("=");
      out.write(token);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 6;
  }
}
