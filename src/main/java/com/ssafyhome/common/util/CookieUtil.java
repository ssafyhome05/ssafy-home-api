package com.ssafyhome.common.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

  public Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(24 * 60 * 60);
    return cookie;
  }

  public Cookie deleteCookie(String key) {

    Cookie cookie = new Cookie(key, null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    return cookie;
  }

  public String convertToString(Cookie cookie) {

    StringBuilder sb = new StringBuilder();
    sb.append(cookie.getName()).append('=').append(cookie.getValue());
    if (cookie.getMaxAge() > 0) {
      sb.append("; Max-Age=").append(cookie.getMaxAge());
    }
    if (cookie.getPath() != null) {
      sb.append("; Path=").append(cookie.getPath());
    }
    if (cookie.isHttpOnly()) {
      sb.append("; HttpOnly");
    }
    if (cookie.getSecure()) {
      sb.append("; Secure");
    }
    return sb.toString();
  }
}
