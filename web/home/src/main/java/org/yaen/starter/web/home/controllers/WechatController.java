package org.yaen.starter.web.home.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.ServletException;
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
 * wechat controller, deals wechat auth/callback
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
	 * simple process request routine
	 * 
	 * @param request
	 * @return
	 */
	private String simpleProcessRequest(HttpServletRequest request) {
		InputStream is = null;
		Map<String, String> requestMap = null;

		// parse input
		try {
			// get input stream
			is = request.getInputStream();

			// parse xml to map
			requestMap = wechatService.parseXml(is);

		} catch (Exception ex) {
			log.error("wechat servlet error:", ex);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
				is = null;
			}
		}

		// call service to handle request
		return wechatService.handleRequest(requestMap);
	}

	/**
	 * wechat callback get, for token check
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// source check from wechat server, return echostr for ok
		log.debug("wechat route auth, return echostr for ok, any other for error.");

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
		PrintWriter writer = resp.getWriter();

		// check signature, return echostr if pass
		if (signature == null || timestamp == null || nonce == null || echostr == null) {
			writer.write("you records has recorded,please leave it now !");
		} else {
			try {
				if (wechatService.checkSignature(token, signature, timestamp, nonce)) {
					log.debug("wechat route auth ok");
					writer.write(echostr);
				}
			} catch (NoSuchAlgorithmException ex) {
				log.error("SHA-1 can not be found", ex);
				writer.write("error!");
			} catch (IllegalArgumentException ex) {
				log.error(
						"wechat route auth fail with bad parameter, ip={}, signature={}, timestamp={}, nonce={}, echostr={}",
						ip, signature, timestamp, nonce, echostr);
				writer.write("error!");
			}
		}

		// close
		writer.close();
	}

	/**
	 * wechat callback post, for messages
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// set encoding to utf-8
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		// get response
		String respMessage = this.simpleProcessRequest(req);

		// write response
		PrintWriter writer = resp.getWriter();
		writer.print(respMessage);
		writer.close();
	}

}
