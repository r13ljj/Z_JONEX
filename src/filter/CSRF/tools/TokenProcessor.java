package filter.CSRF.tools;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TokenProcessor
{
  private static TokenProcessor instance = new TokenProcessor();
  private long previous;

  public static void main(String[] args)
  {
    System.out.println(getInstance().encodeToken("aaa"));
  }

  public static TokenProcessor getInstance() {
    return instance;
  }

  public synchronized boolean isTokenValid(HttpServletRequest request) {
    return isTokenValid(request, false);
  }

  public synchronized boolean isTokenValid(HttpServletRequest request, String name, boolean reset) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return false;
    }
    String uri = request.getRequestURI();
    TokenHash tokenHash = (TokenHash)session.getAttribute(name);
    if (!tokenHash.isExists(uri)) {
      return false;
    }

    String token = request.getParameter(name);
    if (token == null) {
      return false;
    }

    return tokenHash.isValidator(uri, token, reset);
  }

  public synchronized boolean isTokenValid(HttpServletRequest request, boolean reset)
  {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return false;
    }

    String saved = (String)session
      .getAttribute("org.apache.struts.action.TOKEN");
    if (saved == null) {
      return false;
    }

    if (reset) {
      resetToken(request);
    }

    String token = request
      .getParameter("org.apache.struts.taglib.html.TOKEN");
    if (token == null) {
      return false;
    }

    return saved.equals(token);
  }

  public synchronized void resetToken(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute("org.apache.struts.action.TOKEN");
  }

  public synchronized void saveToken(HttpServletRequest request) {
    HttpSession session = request.getSession();
    String token = generateToken(request);
    if (token != null)
      session.setAttribute("org.apache.struts.action.TOKEN", token);
  }

  public synchronized String loadToken(HttpServletRequest request, HttpServletResponse response, String name, boolean isValidRequest)
  {
    String token = request.getAttribute(name) == null ? null : (String)request.getAttribute(name);
    if ((!isValidRequest) && (token == null)) {
      token = generateToken(request);
      request.setAttribute(name, token);
      response.addCookie(new Cookie(name, token));
    } else if (isValidRequest) {
      token = request.getParameter(name);
      request.setAttribute(name, token);
    }
    return token;
  }

  public synchronized String loadToken(HttpServletRequest request, HttpServletResponse response, String name, String url) {
    String token = request.getAttribute(name) == null ? null : (String)request.getAttribute(name);
    if (token == null) {
      token = generateToken(request);
      request.setAttribute(name, token);
      response.addCookie(new Cookie(name, token));
      HttpSession session = request.getSession();
      session.setAttribute(name, encodeToken(url, token));
    }
    return token;
  }

  public synchronized String generateToken(HttpServletRequest request, String name, String uri) {
    HttpSession session = request.getSession();
    TokenHash tokenHash = (TokenHash)session.getAttribute(name);
    String token = generateToken(request, uri);
    if (tokenHash == null) {
      tokenHash = new TokenHash();
    }
    tokenHash.addToken(uri, token);
    session.setAttribute(name, tokenHash);
    return token;
  }

  public synchronized String generateToken(HttpServletRequest request, String uri) {
    HttpSession session = request.getSession();
    try {
      byte[] id = session.getId().getBytes();
      long current = System.currentTimeMillis();
      if (current == this.previous) {
        current += 1L;
      }
      this.previous = current;
      byte[] now = new Long(current).toString().getBytes();
      uri = uri == null ? "null" : uri;
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(id);
      md.update(uri.getBytes());
      md.update(now);
      return toHex(md.digest());
    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
    }
    return null;
  }

  public synchronized String encodeToken(String hash) {
    return encodeToken("", hash);
  }

  public synchronized String encodeToken(String url, String hash) {
    try {
      byte[] code = "innerpw&=".getBytes();
      byte[] burl = url == null ? new byte[0] : url.getBytes();
      byte[] bhash = hash == null ? new byte[0] : hash.getBytes();
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(burl);
      md.update(code);
      md.update(bhash);
      return toHex(md.digest());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  public synchronized String generateToken(HttpServletRequest request) {
    HttpSession session = request.getSession();
    try {
      byte[] id = session.getId().getBytes();
      long current = System.currentTimeMillis();
      if (current == this.previous) {
        current += 1L;
      }
      this.previous = current;
      byte[] now = new Long(current).toString().getBytes();
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(id);
      md.update(now);
      return toHex(md.digest());
    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
    }
    return null;
  }

  private String toHex(byte[] buffer) {
    StringBuffer sb = new StringBuffer(buffer.length * 2);
    for (int i = 0; i < buffer.length; i++) {
      sb.append(Character.forDigit((buffer[i] & 0xF0) >> 4, 16));
      sb.append(Character.forDigit(buffer[i] & 0xF, 16));
    }
    return sb.toString();
  }
}
