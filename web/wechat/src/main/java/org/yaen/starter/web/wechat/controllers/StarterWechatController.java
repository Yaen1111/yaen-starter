package org.yaen.starter.web.wechat.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.PropertiesUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.ComponentMessageEntity;
import org.yaen.starter.core.model.wechat.entities.PlatformMessageEntity;
import org.yaen.starter.core.model.wechat.entities.PlatformUserEntity;
import org.yaen.starter.core.model.wechat.enums.EventTypes;
import org.yaen.starter.core.model.wechat.enums.InfoTypes;
import org.yaen.starter.core.model.wechat.enums.MessageTypes;
import org.yaen.starter.core.model.wechat.models.ComponentMessageModel;
import org.yaen.starter.core.model.wechat.models.ComponentModel;
import org.yaen.starter.core.model.wechat.models.PlatformComponentMessageModel;
import org.yaen.starter.core.model.wechat.models.PlatformMessageModel;
import org.yaen.starter.core.model.wechat.models.PlatformModel;
import org.yaen.starter.core.model.wechat.models.PlatformUserModel;
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
	 * process platform message for given appid
	 * 
	 * @param appid
	 * @param requestMessage
	 * @throws DataException
	 * @throws CommonException
	 * @throws CoreException
	 */
	protected void processPlatformMessage(String appid, PlatformMessageModel requestMessage)
			throws DataException, CommonException, CoreException {

		PlatformMessageEntity msg = requestMessage.getEntity();
		PlatformUserModel puser = null;
		PlatformUserEntity user = null;
		Long now = DateUtil.getNow().getTime();

		if (StringUtil.isNotBlank(msg.getFromUserName())) {
			// has user info
			puser = new PlatformUserModel(this.proxyService);

			// try get one, if not exists, create one
			try {
				puser.loadByOpenId(msg.getFromUserName(), appid);
			} catch (DataNotExistsException ex) {
				// no data, create one
				puser.saveNew();
			}

			// get user entity
			user = puser.getEntity();

			// set last active time
			user.setLastActiveTime(DateUtil.getNow());
		}

		// check event
		if (StringUtil.equals(msg.getMsgType(), MessageTypes.REQ_MESSAGE_TYPE_EVENT) && msg.getEvent() != null) {
			switch (msg.getEvent()) {
			case EventTypes.EVENT_TYPE_SUBSCRIBE:
				// user subscribe
				user.setSubscribe(1);
				user.setSubscribeTime(now);
				break;
			case EventTypes.EVENT_TYPE_UNSUBSCRIBE:
				// user unsubscribe
				user.setSubscribe(0);
				user.setUnsubscribeTime(now);
				break;
			default:
				// ignore others
				break;
			}
		}

		// update user
		if (puser != null) {
			puser.saveById();
		}
	}

	/**
	 * process component message
	 * 
	 * @param requestMessage
	 * @throws DataException
	 * @throws CommonException
	 * @throws CoreException
	 */
	protected void processComponentMessage(ComponentMessageModel requestMessage)
			throws DataException, CommonException, CoreException {

		ComponentMessageEntity msg = requestMessage.getEntity();

		if (msg.getInfoType() != null) {
			switch (msg.getInfoType()) {
			case InfoTypes.COMPONENT_VERIFY_TICKET:
			// verify ticket, update component
			{
				ComponentModel component = new ComponentModel(this.proxyService);

				// try get one, if not exists, create one
				try {
					component.loadById(WechatPropertiesUtil.getComponentAppid());
				} catch (DataNotExistsException ex) {
					// no data, create one
					component.saveNew();
				}

				// set ticket
				component.getEntity().setComponentVerifyTicket(msg.getComponentVerifyTicket());
				component.getEntity().setComponentVerifyTicketCreate(msg.getCreateTime());

				// save
				component.saveById();
			}
				break;
			case InfoTypes.AUTHORIZED:
			case InfoTypes.UPDATEAUTHORIZED:
			// platform auth with auth code
			{
				PlatformModel platform = new PlatformModel(this.proxyService);

				// try get one, if not exists, create one
				try {
					platform.loadById(msg.getAuthorizerAppid());
				} catch (DataNotExistsException ex) {
					// no data, create one
					platform.saveNew();
				}

				// set ticket
				platform.getEntity().setAuthorizationCode(msg.getAuthorizationCode());
				platform.getEntity().setAuthorizationCodeExpiredTime(msg.getAuthorizationCodeExpiredTime());

				// save
				platform.saveById();
			}
				break;
			default:
				// ignore others
				break;
			}
		}
	}

	/**
	 * process platform component message, mainly for subscribe
	 * 
	 * @param appid
	 * @param requestMessage
	 * @throws DataException
	 * @throws CommonException
	 * @throws CoreException
	 */
	protected void processPlatformComponentMessage(String appid, PlatformMessageModel requestMessage)
			throws DataException, CommonException, CoreException {
		// get entity
		PlatformMessageEntity msg = requestMessage.getEntity();

		// check event
		if (StringUtil.equals(msg.getMsgType(), MessageTypes.REQ_MESSAGE_TYPE_EVENT)
				&& StringUtil.equals(msg.getEvent(), EventTypes.EVENT_TYPE_SUBSCRIBE)) {
			// is subscribe

			PlatformUserModel puser = null;
			PlatformUserEntity user = null;
			Long now = DateUtil.getNow().getTime();

			puser = new PlatformUserModel(this.proxyService);

			// try get one, if not exists, create one
			try {
				puser.loadByOpenId(msg.getFromUserName(), appid);
			} catch (DataNotExistsException ex) {
				// no data, create one
				puser.saveNew();
			}

			// get user entity
			user = puser.getEntity();

			// set last active time
			user.setLastActiveTime(DateUtil.getNow());

			// update user
			if (puser != null) {
				puser.saveById();
			}

		} else {
			// as normal
			this.processPlatformMessage(appid, requestMessage);
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

				// process message
				this.processPlatformMessage(WechatPropertiesUtil.getAppid(), requestMessage);

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
		log.info("api:wechat:component:message:called, uri={}, ip={}, method={}, querystring={}",
				request.getRequestURI(), WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());

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

				// param for decrypt
				String msg_signature = request.getParameter("msg_signature");
				String timestamp = request.getParameter("timestamp");
				String nonce = request.getParameter("nonce");

				// load xml from input stream, need decrypt
				requestMessage.loadFromXml(reader, msg_signature, timestamp, nonce);

				// save message anyway
				requestMessage.saveNew();

				// set component/platform info according to message
				this.processComponentMessage(requestMessage);

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
		log.info("api:wechat:platformcom:message:called, uri={}, ip={}, method={}, querystring={}",
				request.getRequestURI(), WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());

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

				// load xml from input stream, need decrypt
				requestMessage.loadFromXml(reader, msg_signature, timestamp, nonce);

				// save message anyway
				requestMessage.saveNew();

				// process message
				this.processPlatformMessage(appid, requestMessage);

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

	/**
	 * preauth page for other platform to bind with component
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("platformcom/preauth.html")
	public String preAuth(Model model, HttpServletRequest request) {
		// log api
		log.info("api:wechat:platformcom:preauth.html:called, uri={}, ip={}, method={}, querystring={}",
				request.getRequestURI(), WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());

		final String API = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=COMPONENT_APPID&pre_auth_code=PRE_AUTH_CODE&redirect_uri=REDIRECT_URI";

		try {

			// need component model
			ComponentModel component = new ComponentModel(this.proxyService);

			model.addAttribute("preauth",
					API.replace("COMPONENT_APPID", component.getComponentAppid())
							.replace("PRE_AUTH_CODE", component.getPreAuthCode())
							.replace("REDIRECT_URI", PropertiesUtil.getProperty("api.platformcom.auth")));

		} catch (DataNotExistsException ex) {
			log.error("api:wechat:platformcom:preauth.html: component not exist", ex);
			model.addAttribute("error", ex);
		} catch (Exception ex) {
			log.error("api:wechat:platformcom:preauth.html:error", ex);
			model.addAttribute("error", ex);
		}

		// show page
		return "wechat/preauth";
	}

	/**
	 * component-binded platform auth callback
	 * 
	 * <pre>
	 * the auth page is :
	 * https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=xxxx&pre_auth_code=xxxxx&redirect_uri=xxxx
	 * </pre>
	 * 
	 * <pre>
	 * when done, the redirect_uri will be called with auth_code
	 * </pre>
	 * 
	 * @param component_appid
	 * @param auth_code
	 * @param expires_in
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "platformcom/auth.html", "platformcom/auth" })
	public String platformComponentAuth(String component_appid, String auth_code, String expires_in,
			HttpServletRequest request, HttpServletResponse response) {
		// log api
		log.info("api:wechat:platformcom:auth.html:called, uri={}, ip={}, method={}, querystring={}",
				request.getRequestURI(), WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());

		// here we can call api to get platform component access token?
		log.info("api:wechat:platformcom:auth  component_appid={}, auth_code={}, expires_in={}", component_appid,
				auth_code, expires_in);

		// show page
		return "wechat/auth";
	}

}
