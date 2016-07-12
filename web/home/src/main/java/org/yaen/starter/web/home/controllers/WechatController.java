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
	public void sign(HttpServletRequest req, HttpServletResponse resp) {

		// source check from wechat server, return echostr for ok
		log.debug("wechat route auth, return echostr for ok, any other for error.");

		// the writer
		PrintWriter writer = null;

		try {

			// the follow is from wechat server
			String signature = req.getParameter("signature");
			String timestamp = req.getParameter("timestamp");
			String nonce = req.getParameter("nonce");
			String echostr = req.getParameter("echostr");

			// get ip
			String ip = WebUtil.getClientIp(req);

			// get token
			String token = PropertiesUtil.getProperty(WECHAT_TOKEN_PROPERTY);

			log.debug("get: ip={}, signature={}, timestamp={}, nonce={}, echostr={}", ip, signature, timestamp, nonce,
					echostr);

			// direct output
			writer = resp.getWriter();

			// check signature, return echostr if pass
			if (wechatService.checkSignature(token, signature, timestamp, nonce)) {
				log.debug("wechat route auth ok");
				writer.write(echostr);
			} else {
				log.debug("wechat route auth signature check failed");
				writer.write("check failed");
			}

		} catch (IllegalArgumentException ex) {
			log.error("wechat route auth fail with bad parameter");
			writer.write("bad parameter");
		} catch (Exception ex) {
			log.error("wechat doGet error", ex);
			resp.setStatus(500);
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
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "message", method = RequestMethod.POST)
	public void message(HttpServletRequest req, HttpServletResponse resp) {

		// the writer
		PrintWriter writer = null;

		// parse input stream as xml
		InputStream is = null;
		Map<String, String> requestMap = null;

		// parse input
		try {

			// set encoding to utf-8
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");

			// get input stream
			is = req.getInputStream();

			// parse xml to map
			requestMap = wechatService.parseXml(is);

			// call service to handle request and get response
			String respMessage = wechatService.handleRequest(requestMap);

			// write response
			writer = resp.getWriter();
			writer.write(respMessage);
		} catch (Exception ex) {
			log.error("wechat servlet error:", ex);
			resp.setStatus(500);
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
