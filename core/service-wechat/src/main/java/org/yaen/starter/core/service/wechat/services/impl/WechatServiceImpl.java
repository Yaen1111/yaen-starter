package org.yaen.starter.core.service.wechat.services.impl;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.wechat.entities.PlatformMessageEntity;
import org.yaen.starter.core.model.wechat.enums.EventTypes;
import org.yaen.starter.core.model.wechat.enums.MessageTypes;
import org.yaen.starter.core.model.wechat.models.PlatformMessageModel;
import org.yaen.starter.core.model.wechat.objects.TextResponseMessage;
import org.yaen.starter.core.model.wechat.services.WechatService;

/**
 * wechat service implement
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class WechatServiceImpl implements WechatService {

	/**
	 * @see org.yaen.starter.core.model.wechat.services.WechatService#checkSignature(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String checkSignature(String token, String signature, String timestamp, String nonce, String echostr) {
		AssertUtil.notBlank(token);
		AssertUtil.notBlank(signature);
		AssertUtil.notBlank(timestamp);
		AssertUtil.notBlank(nonce);
		AssertUtil.notBlank(echostr);

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
			return echostr;
		} else {
			return "bad parameter";
		}
	}

	/**
	 * @see org.yaen.starter.core.model.wechat.services.WechatService#makeResponse(org.yaen.starter.core.model.wechat.models.PlatformMessageModel)
	 */
	@Override
	public String makeResponse(PlatformMessageModel requestMessage) {
		AssertUtil.notNull(requestMessage);

		PlatformMessageEntity req = requestMessage.getEntity();

		// response as text
		TextResponseMessage resp = new TextResponseMessage();
		resp.setMsgType(MessageTypes.RESP_MESSAGE_TYPE_TEXT);
		resp.setToUserName(req.getFromUserName());
		resp.setFromUserName(req.getToUserName());
		resp.setCreateTime(DateUtil.getNow().getTime());

		String msgType = req.getMsgType();
		String respContent = "";

		// 文本消息
		if (msgType.equals(MessageTypes.REQ_MESSAGE_TYPE_TEXT)) {
			respContent = "您发送的是文本消息:" + req.getContent();
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
			String eventType = req.getEvent();
			String eventKey = req.getEventKey();

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

}
