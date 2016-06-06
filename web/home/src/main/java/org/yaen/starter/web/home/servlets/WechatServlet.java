package org.yaen.starter.web.home.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yaen.starter.common.data.enums.wechat.EventTypes;
import org.yaen.starter.common.data.enums.wechat.MessageTypes;
import org.yaen.starter.common.data.objects.wechat.TextResponseMessage;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.PropertiesUtil;
import org.yaen.starter.web.home.utils.WebUtil;
import org.yaen.starter.web.home.utils.WechatMessageUtil;
import org.yaen.starter.web.home.utils.WechatUtil;

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

	public static final String WECHAT_TOKEN_PROPERTY = "wechat.token";

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
				if (WechatUtil.checkSignature(token, signature, timestamp, nonce)) {
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
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// set encoding to utf-8
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		// TODO get response
		String respMessage = this.sampleProcessRequest(req);

		// write response
		PrintWriter writer = resp.getWriter();
		writer.print(respMessage);
		writer.close();
	}

	/**
	 * sample precess request routine
	 * 
	 * @param request
	 * @return
	 */
	@Deprecated
	private String sampleProcessRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";

			// xml请求解析
			Map<String, String> requestMap = WechatMessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 回复文本消息
			TextResponseMessage textResponseMessage = new TextResponseMessage();
			textResponseMessage.setToUserName(fromUserName);
			textResponseMessage.setFromUserName(toUserName);
			textResponseMessage.setCreateTime(DateUtil.getNow().getTime());
			textResponseMessage.setMsgType(MessageTypes.RESP_MESSAGE_TYPE_TEXT);
			textResponseMessage.setFuncFlag(0);

			// 文本消息
			if (msgType.equals(MessageTypes.REQ_MESSAGE_TYPE_TEXT)) {
				respContent = "您发送的是文本消息！";
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
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(EventTypes.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "谢谢您的关注！";
				}
				// 取消订阅
				else if (eventType.equals(EventTypes.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(EventTypes.EVENT_TYPE_CLICK)) {
					// TODO 自定义菜单权没有开放，暂不处理该类消息
				}
			}

			textResponseMessage.setContent(respContent);
			respMessage = WechatMessageUtil.textMessageToXml(textResponseMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}

}
