package filter.parameter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParameterHandlerFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HashMap m = new HashMap(request.getParameterMap());

		if (processParameters(m)) {
			Iterator it = m.keySet().iterator();
			String key = "";
			Object obj = null;
			
			//???
			while (it.hasNext()) {
				key = (String) it.next();
				obj = m.get(key);
				if ((obj instanceof String[])) {
					String[] str = (String[]) obj;
					for (int x = 0; x < str.length; x++);
				}
			}

			ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(
					req, m);
			chain.doFilter(requestWrapper, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean processParameters(HashMap m) {
		boolean flag = false;
		String tmpStr = "";
		try {
			Set<Map.Entry<String, Object>> newp = m.entrySet();
			for (Map.Entry entry : newp) {
				Object obj = entry.getValue();

				if (obj instanceof String) {
					entry.setValue(ParameterTool.Html2Text((String) obj));
				} else if (obj instanceof String[]) {
					String[] params = (String[]) obj;
					if (params != null && params.length > 0) {
						String[] pp = params;
						int x = 0;
						for (String param : pp) {
							tmpStr = ParameterTool.Html2Text(param);
							pp[x] = tmpStr;
							x++;
						}
						entry.setValue(tmpStr);
					}
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
