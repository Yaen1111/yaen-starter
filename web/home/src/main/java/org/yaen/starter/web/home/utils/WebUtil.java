/**
 * 
 */
package org.yaen.starter.web.home.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.util.WebUtils;
import org.yaen.starter.common.util.utils.StringUtil;

/**
 * web util, extends spring web utils
 * 
 * @author Yaen 2016年2月3日下午5:01:17
 */
public class WebUtil extends WebUtils {

	/** JSON_MIME_TYPE */
	private static final String MIME_TYPE_JSON = "application/json;charset=UTF-8";
	private static final String MIME_TYPE_HTML = "text/html;charset=UTF-8";

	/**
	 * get header by given name with default value
	 * 
	 * @param request
	 * @param header
	 * @param defaultValue
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String header, String defaultValue) {
		String result = StringUtil.trimToNull(request.getHeader(header));
		if (StringUtil.isNotEmpty(result)) {
			return result;
		}
		return defaultValue;
	}

	/**
	 * get header by given name, return null if not exists
	 * 
	 * @param request
	 * @param header
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String header) {
		return getHeader(request, header, null);
	}

	/**
	 * get cookie value, if null or not exists, return default value
	 * 
	 * @param request
	 * @param cookieName
	 * @param defaultValue
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, String defaultValue) {
		Cookie cookie = getCookie(request, cookieName);
		if (cookie != null) {
			String value = StringUtil.trimToNull(cookie.getValue());
			if (StringUtil.isNotEmpty(value)) {
				return value;
			}
		}
		return defaultValue;
	}

	/**
	 * get cookie value by given name, return null if not exists
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {

		return getCookieValue(request, cookieName, null);
	}

	/**
	 * get param by given name with default value
	 * 
	 * @param request
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String param, String defaultValue) {
		String result = StringUtil.trimToNull(request.getParameter(param));
		if (StringUtil.isNotEmpty(result)) {
			return result;
		}
		return defaultValue;
	}

	/**
	 * get param by given name, return null if not exists
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String param) {
		return getParam(request, param, null);
	}

	/**
	 * get client ipv4 address
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		// get ip in order
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtil.isBlank(ip) || StringUtil.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtil.isBlank(ip) || StringUtil.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtil.isBlank(ip) || StringUtil.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getRemoteAddr();

			// if is client is self, get local ip from inet info
			if (StringUtil.equals(ip, "127.0.0.1")) {
				try {
					InetAddress inet = null;
					inet = InetAddress.getLocalHost();
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {
					// ignore exception
				}
			}
		}

		// if is multi agent, the first ip is real one, seperated by commer
		if (ip != null && ip.indexOf(",") > 0) {
			ip = ip.substring(0, ip.indexOf(","));
		}
		return ip;
	}

	/**
	 * get server ip by local interface
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getServerIp() throws SocketException {
		String serverIp = "";
		Enumeration<NetworkInterface> en = null;

		// get all net
		en = NetworkInterface.getNetworkInterfaces();
		while (en.hasMoreElements()) {
			NetworkInterface network = en.nextElement();
			Enumeration<InetAddress> enAddr = network.getInetAddresses();
			while (enAddr.hasMoreElements()) {
				InetAddress addr = enAddr.nextElement();
				if (addr instanceof Inet4Address) {
					if (!StringUtil.equals(addr.getHostAddress(), "127.0.0.1")) {
						serverIp = addr.getHostAddress();
						break;
					}
				}
			}
		}

		return serverIp;
	}

	/**
	 * write content to response, use typed version if possible
	 * 
	 * @param response
	 * @param content
	 * @param mimeType
	 * @throws IOException
	 */
	public static void writeToResponse(HttpServletResponse response, String content, String mimeType)
			throws IOException {
		// set mime type
		response.setContentType(mimeType);

		// use writer
		// response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(content);
			writer.flush();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * write data to repsonse, use typed version if possible
	 * 
	 * @param response
	 * @param data
	 * @param mimeType
	 * @throws IOException
	 */
	public static void writeToResponse(HttpServletResponse response, byte[] data, String mimeType) throws IOException {
		// set mime type
		response.setContentType(mimeType);

		// use stream
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	/**
	 * write html to response
	 * 
	 * @param response
	 * @param message
	 * @throws IOException
	 */
	public static void writeHtmlToResponse(HttpServletResponse response, String message) throws IOException {
		writeToResponse(response, message, MIME_TYPE_HTML);
	}

	/**
	 * write json to response
	 * 
	 * @param response
	 * @param message
	 * @throws IOException
	 */
	public static void writeJsonToResponse(HttpServletResponse response, String message) throws IOException {
		writeToResponse(response, message, MIME_TYPE_JSON);
	}

	/**
	 * write refresh to response
	 * 
	 * @param response
	 * @param redirectURL
	 * @throws IOException
	 */
	public static void writeRefreshToResponse(HttpServletResponse response) throws IOException {
		response.sendRedirect("");
	}

}
