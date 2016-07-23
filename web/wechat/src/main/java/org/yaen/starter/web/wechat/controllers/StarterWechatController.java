package org.yaen.starter.web.wechat.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
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
import org.yaen.starter.core.model.wechat.objects.TextResponseMessage;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;
import org.yaen.starter.web.home.utils.WebUtil;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

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

			String result = "signature fail";

			if (StringUtil.isBlank(token) || StringUtil.isBlank(signature) || StringUtil.isBlank(timestamp)
					|| StringUtil.isBlank(nonce) || StringUtil.isBlank(echostr)) {
				result = "bad parameter";
			} else {
				// add token, timestamp, nonce to array, and sort
				String[] arr = new String[] { token, timestamp, nonce };
				Arrays.sort(arr);

				// combine together
				StringBuilder content = new StringBuilder();
				for (int i = 0; i < arr.length; i++) {
					content.append(arr[i]);
				}

				// do sha-1
				String tmpStr = DigestUtils.sha1Hex(content.toString());

				// should be same
				if (StringUtil.equalsIgnoreCase(tmpStr, signature)) {
					result = echostr;
				}
			}

			log.debug("doCheckSignature, result={}", result);

			// write result
			writer.write(result);

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
	 * process platform message for given appid, give response message
	 * 
	 * @param appid
	 * @param requestMessage
	 * @throws DataException
	 * @throws CommonException
	 * @throws CoreException
	 */
	protected void processPlatformMessage(String appid, PlatformMessageModel requestMessage)
			throws DataException, CommonException, CoreException {

		// save message and related
		PlatformMessageEntity msg = requestMessage.getEntity();
		PlatformUserModel puser = null;
		PlatformUserEntity user = null;
		Long now = DateUtil.getNow().getTime();

		if (StringUtil.isNotBlank(msg.getFromUserName())) {
			// has user info
			puser = new PlatformUserModel(this.proxyService);

			// try get one, if not exists, create one
			puser.loadOrCreateByOpenId(msg.getFromUserName(), appid);

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
	 * make response to message
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected String responsePlatformMessage(PlatformMessageModel requestMessage) {

		PlatformMessageEntity msg = requestMessage.getEntity();

		// response as text
		TextResponseMessage resp = new TextResponseMessage();
		resp.setMsgType(MessageTypes.RESP_MESSAGE_TYPE_TEXT);
		resp.setToUserName(msg.getFromUserName());
		resp.setFromUserName(msg.getToUserName());
		resp.setCreateTime(DateUtil.getNow().getTime());

		String msgType = msg.getMsgType();
		String respContent = "";

		// 文本消息
		if (msgType.equals(MessageTypes.REQ_MESSAGE_TYPE_TEXT)) {
			respContent = "您发送的是文本消息:" + msg.getContent();
		}
		// 图片消息
		else if (msgType.equals(MessageTypes.REQ_MESSAGE_TYPE_IMAGE)) {
			respContent = "您发送的是图片消息！";
		}
		// 地理位置消息
		else if (msgType.equals(MessageTypes.REQ_MESSAGE_TYPE_LOCATION)) {
			respContent = "您发送的是地理位置消息！";
		}
		// 链接消息
		else if (msgType.equals(MessageTypes.REQ_MESSAGE_TYPE_LINK)) {
			respContent = "您发送的是链接消息！";
		}
		// 音频消息
		else if (msgType.equals(MessageTypes.REQ_MESSAGE_TYPE_VOICE)) {
			respContent = "您发送的是音频消息！";
		}
		// 事件推送
		else if (msgType.equals(MessageTypes.REQ_MESSAGE_TYPE_EVENT)) {
			// 事件类型
			String eventType = msg.getEvent();
			String eventKey = msg.getEventKey();

			// 订阅
			if (eventType.equals(EventTypes.EVENT_TYPE_SUBSCRIBE)) {
				respContent = "谢谢您的关注！";
			}
			// 取消订阅
			else if (eventType.equals(EventTypes.EVENT_TYPE_UNSUBSCRIBE)) {
				// 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				respContent = "您已经取消关注！";
			}
			// 自定义菜单点击事件
			else if (eventType.equals(EventTypes.EVENT_TYPE_CLICK)) {
				// 事件KEY值，与创建自定义菜单时指定的KEY值对应

				if (eventKey.equals("11")) {
					respContent = "天气预报菜单项被点击！";
				} else if (eventKey.equals("12")) {
					respContent = "公交查询菜单项被点击！";
				} else if (eventKey.equals("13")) {
					respContent = "周边搜索菜单项被点击！";
				} else if (eventKey.equals("14")) {
					respContent = "历史上的今天菜单项被点击！";
				} else if (eventKey.equals("21")) {
					respContent = "歌曲点播菜单项被点击！";
				} else if (eventKey.equals("22")) {
					respContent = "经典游戏菜单项被点击！";
				} else if (eventKey.equals("23")) {
					respContent = "美女电台菜单项被点击！";
				} else if (eventKey.equals("24")) {
					respContent = "人脸识别菜单项被点击！";
				} else if (eventKey.equals("25")) {
					respContent = "聊天唠嗑菜单项被点击！";
				} else if (eventKey.equals("31")) {
					respContent = "Q友圈菜单项被点击！";
				} else if (eventKey.equals("32")) {
					respContent = "电影排行榜菜单项被点击！";
				} else if (eventKey.equals("33")) {
					respContent = "幽默笑话菜单项被点击！";
				} else {
					respContent = "menu clicked key=" + eventKey;
				}
			}
		}

		// set content
		resp.setContent(respContent);

		// get xml
		return requestMessage.toXml(resp);
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
				component.loadOrCreateById(WechatPropertiesUtil.getComponentAppid());

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
				platform.loadOrCreateById(msg.getAuthorizerAppid());

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
	 * response component message, just success is ok
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected String responseComponentMessage(ComponentMessageModel requestMessage) {
		return "success";
	}

	/**
	 * process platform component message, base just bridge to processPlatformMessage
	 * 
	 * @param appid
	 * @param requestMessage
	 * @throws DataException
	 * @throws CommonException
	 * @throws CoreException
	 */
	protected void processPlatformComponentMessage(String appid, PlatformMessageModel requestMessage)
			throws DataException, CommonException, CoreException {
		// just as normal platform
		this.processPlatformMessage(appid, requestMessage);
	}

	/**
	 * response platform component message
	 * 
	 * @param appid
	 * @param requestMessage
	 * @return
	 * @throws CoreException
	 */
	protected String responsePlatformComponentMessage(String appid, PlatformMessageModel requestMessage)
			throws CoreException {
		String resp = this.responsePlatformMessage(requestMessage);
		try {
			// make crypt
			WXBizMsgCrypt pc = new WXBizMsgCrypt(WechatPropertiesUtil.getComponentToken(),
					WechatPropertiesUtil.getComponentAesKey(), appid);

			// need encrypt
			return pc.encryptMsg(resp, StringUtil.toString(DateUtil.getNow().getTime()),
					StringUtil.toString(Math.round(Math.random() * 1000000)));

		} catch (AesException ex) {
			throw new CoreException("aes error", ex);
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
				PlatformMessageModel requestMessage = new PlatformMessageModel(proxyService);

				// get input stream
				reader = request.getReader();

				// load xml from input stream
				requestMessage.loadFromXml(reader);

				// save message anyway
				requestMessage.saveNew();

				// process message
				this.processPlatformMessage(WechatPropertiesUtil.getAppid(), requestMessage);

				// get response as xml string
				String responseString = this.responsePlatformMessage(requestMessage);

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
				ComponentMessageModel requestMessage = new ComponentMessageModel(proxyService);

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
				String responseString = this.responseComponentMessage(requestMessage);

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
				PlatformComponentMessageModel requestMessage = new PlatformComponentMessageModel(proxyService, appid);

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

				// process com message
				this.processPlatformComponentMessage(appid, requestMessage);

				// get response as normal
				String responseString = this.responsePlatformComponentMessage(appid, requestMessage);

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
