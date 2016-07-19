package org.yaen.starter.web.wechat.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.models.PlatformMessageModel;
import org.yaen.starter.core.model.wechat.services.WechatService;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;
import org.yaen.starter.web.home.utils.WebUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * base wechat callback controller, deals wechat auth/callback
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
	 * platform sign, get, for token check, return echostr for ok, any other for error, no exception
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "message", method = RequestMethod.GET)
	public void getPlatformMessage(HttpServletRequest request, HttpServletResponse response) {

		// get ip
		String ip = WebUtil.getClientIp(request);

		// the follow is from wechat server
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		// get token
		String token = WechatPropertiesUtil.getToken();

		// source check from wechat server, return echostr for ok
		log.debug(
				"wechat:getPlatformMessage:called, uri={}, ip={}, signature={}, timestamp={}, nonce={}, echostr={}, token={}",
				request.getRequestURI(), ip, signature, timestamp, nonce, echostr, token);

		// the writer
		PrintWriter writer = null;

		try {

			// direct output
			writer = response.getWriter();

			// check signature, return echostr if pass
			if (wechatService.checkSignature(token, signature, timestamp, nonce)) {
				log.debug("wechat:getPlatformMessage:ok, echostr={}", echostr);
				writer.write(echostr);
			} else {
				log.debug("wechat:getPlatformMessage:failed:signature check fail");
				writer.write("fail");
			}

		} catch (IllegalArgumentException ex) {
			log.error("wechat:getPlatformMessage:failed:bad parameter");
			writer.write("bad parameter");
		} catch (Exception ex) {
			log.error("wechat:getPlatformMessage:error", ex);
			writer.write("system error");
			if (log.isDebugEnabled()) {
				ex.printStackTrace(writer);
			}
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
	public void postPlatformMessage(HttpServletRequest request, HttpServletResponse response) {

		// get ip
		String ip = WebUtil.getClientIp(request);

		// post message call
		log.debug("wechat:postPlatformMessage:called, uri={}, ip={}", request.getRequestURI(), ip);

		// the writer
		PrintWriter writer = null;

		// parse input stream as xml
		InputStream is = null;

		// parse input
		try {

			// set encoding to utf-8
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			// create model to handle
			PlatformMessageModel requestMessage = new PlatformMessageModel(proxyService, wechatService);

			// get input stream
			is = request.getInputStream();

			// load xml from input stream
			requestMessage.loadFromXml(is);

			// save message anyway
			requestMessage.saveNew();

			// make response, big routine
			PlatformMessageModel responseMessage = requestMessage.makeResponse();

			// get response as xml string
			String responseString = responseMessage.toXml();

			log.debug("wechat:message:ok, responseMessage={}", responseString);

			// write response
			writer = response.getWriter();
			writer.write(responseString);
			writer.close();
			writer = null;
		} catch (Exception ex) {
			log.error("wechat:message:error:", ex);
			try {
				writer = response.getWriter();
				writer.write("error");
				if (log.isDebugEnabled()) {
					ex.printStackTrace(writer);
				}
				writer.close();
				writer = null;
			} catch (IOException ex2) {
				log.error("close writer error:", ex2);
			}
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

	/**
	 * wechat component message post, mostly for verify tickets
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "cmessage", method = RequestMethod.POST)
	public void postComponentMessage(HttpServletRequest request, HttpServletResponse response) {

		// get ip
		String ip = WebUtil.getClientIp(request);

		// post message call
		log.debug("wechat:postComponentMessage:called, uri={}, ip={}", request.getRequestURI(), ip);

		// the writer
		PrintWriter writer = null;

		// parse input stream as xml
		InputStream is = null;

		// parse input
		try {

			// set encoding to utf-8
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			// need decrypt TODO

			// save to message, update component info

			log.debug("wechat:postComponentMessage:ok, responseMessage={}", "");

			// write response
			writer = response.getWriter();
			writer.write("success");
			writer.close();
			writer = null;
		} catch (Exception ex) {
			log.error("wechat:message:error:", ex);
			try {
				writer = response.getWriter();
				writer.write("error");
				if (log.isDebugEnabled()) {
					ex.printStackTrace(writer);
				}
				writer.close();
				writer = null;
			} catch (IOException ex2) {
				log.error("close writer error:", ex2);
			}
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
