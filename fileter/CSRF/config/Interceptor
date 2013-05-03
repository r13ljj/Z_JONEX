package filter.CSRF.config;

import java.util.ArrayList;
import java.util.List;

public class Interceptor
{
  private String uri;
  private List<String> methods;
  private List<String> queryStrings;

  public Interceptor()
  {
    this.methods = new ArrayList();
    this.queryStrings = new ArrayList();
  }

  public String getUri() {
    return this.uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void addQueryStrings(String queryString) {
    this.queryStrings.add(queryString);
  }

  public List<String> getQueryStrings() {
    return this.queryStrings;
  }

  public void setQueryStrings(List<String> queryStrings) {
    this.queryStrings = queryStrings;
  }

  public void addMethod(String method) {
    this.methods.add(method);
  }

  public List<String> getMethods() {
    return this.methods;
  }

  public void setMethods(List<String> methods) {
    this.methods = methods;
  }
}
