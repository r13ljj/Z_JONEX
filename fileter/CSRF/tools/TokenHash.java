package filter.CSRF.tools;

import java.util.HashMap;
import java.util.Map;

public class TokenHash
{
  private Map<String, Map<String, String>> tokenHashs;

  public TokenHash()
  {
    this.tokenHashs = new HashMap();
  }

  public void addToken(String uri, String token) {
    Map tokens = (Map)this.tokenHashs.get(uri);
    if (tokens == null) {
      tokens = new HashMap();
      this.tokenHashs.put(uri, tokens);
    }
    tokens.put(token, token);
  }

  public boolean isExists(String uri) {
    return this.tokenHashs.containsKey(uri);
  }

  public boolean isValidator(String uri, String token, boolean reset) {
    Map tokens = (Map)this.tokenHashs.get(uri);
    if ((tokens != null) && (tokens.containsKey(token))) {
      if (reset) {
        tokens.remove(token);
        if (tokens.size() == 0) {
          this.tokenHashs.remove(uri);
        }
      }
      return true;
    }
    return false;
  }
}
