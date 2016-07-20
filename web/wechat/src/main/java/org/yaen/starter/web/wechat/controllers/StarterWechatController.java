package org.yaen.starter.web.wechat.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.models.ComponentMessageModel;
import org.yaen.starter.core.model.wechat.models.PlatformComponentMessageModel;
import org.yaen.starter.core.model.wechat.models.PlatformMessageModel;
import org.yaen.starter.core.model.wechat.services.WechatService;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;
import org.yaen.starter.web.home.utils.WebUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * base wechat callback controller, deals wechat auth/callback, each url need echo check
 * <p>
 * platform message: used for normal platform receiving user message
 * <p>
 * component message: used for component receiving system message
 * <p>
 * component-platform message: used for component receiving binded-platform user message
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Slf4j
@Controller
@RequestMapping("/wechat")
public class StarterWechatController {

	@Autowired
	private ProxyService proxyService;

	@Autowired
	private WechatService wechatService;

	/**
	 * do check signature, the token maybe changed for platform and component
	 * 
	 * @param request
	 * @param response
	 * @param token
	 */
	protected void doCheckSignature(HttpServletRequest request, HttpServletResponse response, String token) {

		// the follow is from wechat server
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		// source check from wechat server, return echostr for ok
		log.debug("doCheckSignature, signature={}, timestamp={}, nonce={}, echostr={}", signature, timestamp, nonce,
				echostr);

		// the writer
		PrintWriter writer = null;

		try {
			// direct output
			writer = response.getWriter();

			try {
				// get result
				String result = wechatService.checkSignature(token, signature, timestamp, nonce, echostr);

				log.debug("doCheckSignature, result={}", result);

				// write result
				writer.write(result);

			} catch (Exception ex) {
				log.error("doCheckSignature error", ex);
				writer.write("system error");
			}

		} catch (Exception ex) {
			log.error("doCheckSignature writer error", ex);
		} finally {
			// close
			if (writer != null) {
				writer.close();
				writer = null;
			}
		}
	}

	/**
	 * platform message, get for token check, post for message data
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("platform/message")
	public void platformMessage(HttpServletRequest request, HttpServletResponse response) {
		// log api
		log.info("api:wechat:platform:message:called, uri={}, ip={}, method={}, querystring={}",
				request.getRequestURI(), WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());

		// check method
		if (StringUtil.equalsIgnoreCase(request.getMethod(), "GET")) {
			// do check signature for platform
			this.doCheckSignature(request, response, WechatPropertiesUtil.getToken());

		} else if (StringUtil.equalsIgnoreCase(request.getMethod(), "POST")) {
			// is message

			// the writer
			PrintWriter writer = null;

			// the body reader
			Reader reader = null;

			try {
				// set encoding to utf-8
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");

				// create model to handle
				PlatformMessageModel requestMessage = new PlatformMessageModel(proxyService, wechatService);

				// get input stream
				reader = request.getReader();

				// load xml from input stream
				requestMessage.loadFromXml(reader);

				// save message anyway
				requestMessage.saveNew();

				// get response as xml string
				String responseString = requestMessage.makeResponse();

				// write response
				writer = response.getWriter();
				writer.write(responseString);

			} catch (Exception ex) {
				log.error("api:wechat:platform:message:error", ex);
			} finally {
				// close
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
					}
					reader = null;
				}

				if (writer != null) {
					writer.close();
					writer = null;
				}
			}
		} // method == post

		// done, other method is ignored
	}

	/**
	 * component message, get for token check, post for verify info
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("component/message")
	public void componentMessage(HttpServletRequest request, HttpServletResponse response) {
		// log api
		log.info("api:wechat:platform:message:called, uri={}, ip={}, method={}, querystring={}",
				request.getRequestURI(), WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());

		// log param
		if (log.isDebugEnabled()) {
			log.debug("querystring={}", request.getQueryString());
		}

		// check method
		if (StringUtil.equalsIgnoreCase(request.getMethod(), "GET")) {
			// do check signature for component
			this.doCheckSignature(request, response, WechatPropertiesUtil.getComponentToken());

		} else if (StringUtil.equalsIgnoreCase(request.getMethod(), "POST")) {
			// is message

			// the writer
			PrintWriter writer = null;

			// the body reader
			Reader reader = null;

			try {
				// set encoding to utf-8
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");

				// create model to handle
				ComponentMessageModel requestMessage = new ComponentMessageModel(proxyService, wechatService);

				// get reader
				reader = request.getReader();

				// load xml from input stream
				requestMessage.loadFromXml(reader);

				// save message anyway
				requestMessage.saveNew();

				// response always is success
				String responseString = "success";

				// write response
				writer = response.getWriter();
				writer.write(responseString);

			} catch (Exception ex) {
				log.error("api:wechat:component:message:error:", ex);
			} finally {
				// close
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
					}
					reader = null;
				}

				if (writer != null) {
					writer.close();
					writer = null;
				}
			}
		} // method == post

		// done, other method is ignored
	}

	/**
	 * component-binded platform message, get for token check, post for message data
	 * 
	 * @param appid
	 * @param request
	 * @param response
	 */
	@RequestMapping("platformcom/{appid}/message")
	public void platformComponentMessage(@PathVariable("appid") String appid, HttpServletRequest request,
			HttpServletResponse response) {
		// log api
		log.info("api:wechat:platform:message:called, uri={}, ip={}, method={}, querystring={}",
				request.getRequestURI(), WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());

		// log param
		if (log.isDebugEnabled()) {
			log.debug("querystring={}", request.getQueryString());
		}

		// check method
		if (StringUtil.equalsIgnoreCase(request.getMethod(), "GET")) {
			// do check signature for platform
			this.doCheckSignature(request, response, WechatPropertiesUtil.getComponentToken());

		} else if (StringUtil.equalsIgnoreCase(request.getMethod(), "POST")) {
			// is message

			// the writer
			PrintWriter writer = null;

			// the body reader
			Reader reader = null;

			try {
				// set encoding to utf-8
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");

				// create model to handle
				PlatformComponentMessageModel requestMessage = new PlatformComponentMessageModel(proxyService,
						wechatService, appid);

				// get reader
				reader = request.getReader();

				// param for decrypt
				String msg_signature = request.getParameter("msg_signature");
				String timestamp = request.getParameter("timestamp");
				String nonce = request.getParameter("nonce");

				// source check from wechat server, return echostr for ok
				log.debug("decrypt, appid={}, msg_signature={}, timestamp={}, nonce={}", appid, msg_signature,
						timestamp, nonce);

				// load xml from input stream, need decrypt
				requestMessage.loadFromXml(reader, msg_signature, timestamp, nonce);

				// save message anyway
				requestMessage.saveNew();

				// get response as normal
				String responseString = requestMessage.makeResponse();

				// write response
				writer = response.getWriter();
				writer.write(responseString);

			} catch (Exception ex) {
				log.error("api:wechat:platformcom:message:error", ex);
			} finally {
				// close
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
					}
					reader = null;
				}

				if (writer != null) {
					writer.close();
					writer = null;
				}
			}
		} // method == post

		// done, other method is ignored
	}

}
