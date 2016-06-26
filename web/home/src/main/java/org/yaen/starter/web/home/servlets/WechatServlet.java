package org.yaen.starter.web.home.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.yaen.starter.common.util.utils.PropertiesUtil;
import org.yaen.starter.core.model.services.WechatService;
import org.yaen.starter.web.home.utils.WebUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * wechat servlet
 * <p>
 * used to response wechat server auth
 * 
 * @author Yaen 2016年6月6日下午10:06:54
 */
@Slf4j
public class WechatServlet extends HttpServlet {
	private static final long serialVersionUID = 4068700881623683064L;

	/** the token for verify server response */
	public static final String WECHAT_TOKEN_PROPERTY = "wechat.token";

	/** the appid for the wechat platform */
	public static final String WECHAT_APPID_PROPERTY = "wechat.appid";

	/** the secret for communicate with wechat server */
	public static final String WECHAT_SECRET_PROPERTY = "wechat.secret";

	@Autowired
	private WechatService wechatService;

	/**
	 * empty constructor
	 */
	public WechatServlet() {
		super();
	}

	/**
	 * wechat server will send token by get for route auth, return echostr for ok
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
	 * menu/send response will be posted
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
}
