package com.ssafyhome.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

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

  public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    
    if (cookies != null && cookies.length > 0) {
      return Arrays.stream(cookies)
              .filter(cookie -> name.equals(cookie.getName()))
              .findFirst();
    }
    
    return Optional.empty();
  }

  public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    
    if (cookies != null && cookies.length > 0) {
      Arrays.stream(cookies)
              .filter(cookie -> name.equals(cookie.getName()))
              .findFirst()
              .ifPresent(cookie -> {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setSecure(true);
                response.addCookie(cookie);
              });
    }
  }
}
