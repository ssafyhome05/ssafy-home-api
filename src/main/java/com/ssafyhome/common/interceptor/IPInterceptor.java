package com.ssafyhome.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class IPInterceptor implements HandlerInterceptor {

	public static final String IPV6_ATTRIBUTE_KEY = "CLIENT_IDV6";
	public static final String IPV4_ATTRIBUTE_KEY = "CLIENT_IPV4";
	public static final int IPV6 = 6;
	public static final int IPV4 = 4;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String ip = extractIP(request);
		request.setAttribute(IPV6_ATTRIBUTE_KEY, convert(ip,IPV6));
		request.setAttribute(IPV4_ATTRIBUTE_KEY, convert(ip,IPV4));
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	private String extractIP(HttpServletRequest request) {

		String[] headers = {
				"X-Forwarded-For",
				"Proxy-Client-IP",
				"WL-Proxy-Client-IP",
				"HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR",
				"HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP",
				"HTTP_CLIENT_CLUSTER_IP",
				"HTTP_FORWARDED_FOR",
				"HTTP_FORWARDED"
		};

		for (String header : headers) {
			String ip = request.getHeader(header);
			if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
				if (ip.contains(",")) {
					ip = ip.split(",")[0];
				}
				return ip;
			}
		}

		return request.getRemoteAddr();
	}

	private String convert(String ip, int ver) {

		String ipv6;
		String ipv4;

		if (ip.contains(":")) {
			ipv6 = ip;

			if (ip.equals("0:0:0:0:0:0:0:1")) {
				ipv4 = "127.0.0.1";
			} else if (ip.startsWith("::ffff:")) {
				return ip.substring("::ffff:".length());
			} else if (ip.startsWith("0:0:0:0:0:ffff:")) {
				return ip.substring("0:0:0:0:0:0:ffff:".length());
			} else {
				ipv4 = ip;
			}
		}
		else {
			ipv4 = ip;
			ipv6 = "::ffff:" + ip;
		}

		return ver == IPV6 ? ipv6 : ipv4;
	}
}
