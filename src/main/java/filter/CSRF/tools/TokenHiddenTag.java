package filter.CSRF.tools;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import filter.CSRF.config.Configuration;

public class TokenHiddenTag extends TagSupport {
	private String uri;

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
		String token = TokenProcessor.getInstance().generateToken(request, Configuration.getInstance().getTokenName(), request.getContextPath() + this.uri);
		try {
			PrintWriter out = response.getWriter();
			out.write("<input type=\"hidden\" value=\"");
			out.write(token);
			out.write("\" name=\"");
			out.write(Configuration.getInstance().getTokenName());
			out.write("\" />");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
