package filter.parameter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {
	private Map params;

	public ParameterRequestWrapper(HttpServletRequest req, Map params) {
		super(req);
		this.params = params;
	}

	public Map getParameterMap() {
		return this.params;
	}

	public Enumeration getParameterNames() {
		Vector l = new Vector(this.params.keySet());

		return l.elements();
	}

	public String[] getParameterValues(String name) {
		Object v = this.params.get(name);

		if (v == null) {
			return null;
		}
		if ((v instanceof String[])) {
			return (String[]) v;
		}
		if ((v instanceof String)) {
			return new String[] { (String) v };
		}

		return new String[] { v.toString() };
	}

	public String getParameter(String name) {
		Object v = this.params.get(name);

		if (v == null) {
			return null;
		}
		if ((v instanceof String[])) {
			String[] strArr = (String[]) v;

			if (strArr.length > 0) {
				return strArr[0];
			}

			return null;
		}

		if ((v instanceof String)) {
			return (String) v;
		}

		return v.toString();
	}
}
