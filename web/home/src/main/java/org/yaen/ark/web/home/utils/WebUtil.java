/**
 * 
 */
package org.yaen.ark.web.home.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;
import org.yaen.spring.common.utils.StringUtil;

/**
 * web util, extends spring web utils
 * 
 * @author xl 2016年2月3日下午5:01:17
 */
public class WebUtil extends WebUtils {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

	/** JSON_MIME_TYPE */
	private static final String MIME_TYPE_JSON = "application/json;charset=UTF-8";

	/** HEADER_CLIENT_IP */
	private static final String HEADER_CLIENT_IP = "";

	private static final Map<String, String> MIME_TYPE_MAP = new HashMap<String, String>();

	static {
		MIME_TYPE_MAP.put("jpg", "image/jpeg");
		MIME_TYPE_MAP.put("jpeg", "image/jpeg");
		MIME_TYPE_MAP.put("gif", "image/gif");
		MIME_TYPE_MAP.put("png", "image/png");
	}

	/**
	 * 根据后缀名取得对应的MimeType
	 * 
	 * @param suffix
	 * @return
	 */
	public static String getMimeType(String suffix) {
		suffix = StringUtil.trimToEmpty(suffix).toLowerCase();
		return MIME_TYPE_MAP.get(suffix);
	}

	/**
	 * 将json字符串写入http响应中
	 * 
	 * @param response
	 * @param message
	 */
	public static void writeJsonToResponse(HttpServletResponse response, String message) {
		writeToResponse(response, message, MIME_TYPE_JSON);
	}

	/**
	 * 将字符串写入http响应中
	 * 
	 * @param response
	 * @param message
	 * @param mimeType
	 */
	public static void writeToResponse(HttpServletResponse response, String message, String mimeType) {
		response.setContentType(mimeType);
		// response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(message);
			writer.flush();
		} catch (IOException e) {
			logger.error("writeJsonToResponse error!", e);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * 将data写入http响应中
	 * 
	 * @param response
	 * @param data
	 * @param mimeType
	 */
	public static void writeToResponse(HttpServletResponse response, byte[] data, String mimeType) {
		response.setContentType(mimeType);
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
		} catch (IOException e) {
			logger.error("writeJsonToResponse error!", e);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	/**
	 * 从HttpServletRequest中取指定名称的header
	 * 
	 * @param request
	 * @param header
	 * @param defaultValue
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String header, String defaultValue) {
		String result = StringUtil.trimToNull(request.getHeader(header));
		if (StringUtil.isEmpty(result)) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 从HttpServletRequest中取指定名称的header
	 * 
	 * @param request
	 * @param header
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String header) {
		return StringUtil.trimToNull(request.getHeader(header));
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
	 * get cookie value, if not exists, return null
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		return getCookieValue(request, cookieName);
	}

	/**
	 * 从HttpServletRequest中取指定名称的参数
	 * 
	 * @param request
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String param, String defaultValue) {
		String result = StringUtil.trimToNull(request.getParameter(param));
		if (StringUtil.isEmpty(result)) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 从HttpServletRequest中取指定名称的参数
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String param) {
		return StringUtil.trimToNull(request.getParameter(param));
	}

	/**
	 * 取得客户端ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String clientIp = getHeader(request, HEADER_CLIENT_IP, null);
		if (StringUtil.isEmpty(clientIp)) {
			clientIp = request.getRemoteAddr();
		}
		return clientIp;
	}
}
