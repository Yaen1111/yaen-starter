package org.yaen.starter.web.home.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yaen.starter.common.util.utils.PropertiesUtil;
import org.yaen.starter.core.model.services.WechatService;
import org.yaen.starter.web.home.utils.WebUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * wechat callback controller, deals wechat auth/callback
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Slf4j
@Controller
@RequestMapping("/wechat")
public class WechatController {

	/** the token for verify server response */
	public static final String WECHAT_TOKEN_PROPERTY = "wechat.token";

	/** the appid for the wechat platform */
	public static final String WECHAT_APPID_PROPERTY = "wechat.appid";

	/** the secret for communicate with wechat server */
	public static final String WECHAT_SECRET_PROPERTY = "wechat.secret";

	@Autowired
	private WechatService wechatService;

	/**
	 * wechat sign, get, for token check, return echostr for ok, any other for error, no exception
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "sign", method = RequestMethod.GET)
	public void sign(HttpServletRequest request, HttpServletResponse response) {

		// get ip
		String ip = WebUtil.getClientIp(request);

		// the follow is from wechat server
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		// get token
		String token = PropertiesUtil.getProperty(WECHAT_TOKEN_PROPERTY);

		// source check from wechat server, return echostr for ok
		log.debug("wechat:sign:called, uri={}, ip={}, signature={}, timestamp={}, nonce={}, echostr={}, token={}",
				request.getRequestURI(), ip, signature, timestamp, nonce, echostr, token);

		// the writer
		PrintWriter writer = null;

		try {

			// direct output
			writer = response.getWriter();

			// check signature, return echostr if pass
			if (wechatService.checkSignature(token, signature, timestamp, nonce)) {
				log.debug("wechat:sign:ok, echostr={}", echostr);
				writer.write(echostr);
			} else {
				log.debug("wechat:sign:failed:signature check fail");
				writer.write("fail");
			}

		} catch (IllegalArgumentException ex) {
			log.error("wechat:sign:failed:bad parameter");
			writer.write("bad parameter");
		} catch (Exception ex) {
			log.error("wechat:sign:error", ex);
			response.setStatus(500);
		} finally {
			// close
			if (writer != null) {
				writer.close();
				writer = null;
			}
		}
	}

	/**
	 * wechat message post, for messages
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "message", method = RequestMethod.POST)
	public void message(HttpServletRequest request, HttpServletResponse response) {

		// get ip
		String ip = WebUtil.getClientIp(request);

		// post message call
		log.debug("wechat:message:called, uri={}, ip={}", request.getRequestURI(), ip);

		// the writer
		PrintWriter writer = null;

		// parse input stream as xml
		InputStream is = null;
		Map<String, String> requestMap = null;

		// parse input
		try {

			// set encoding to utf-8
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			// get input stream
			is = request.getInputStream();

			// parse xml to map
			requestMap = wechatService.parseXml(is);

			log.debug("wechat:message:parseXml, requestmap={}", requestMap);

			// call service to handle request and get response
			String respMessage = wechatService.handleRequest(requestMap);

			log.debug("wechat:message:ok, respMessage={}", respMessage);

			// write response
			writer = response.getWriter();
			writer.write(respMessage);
		} catch (Exception ex) {
			log.error("wechat:message:error:", ex);
			response.setStatus(500);
		} finally {
			// close
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
				is = null;
			}

			if (writer != null) {
				writer.close();
				writer = null;
			}
		}
	}

}
