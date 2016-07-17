package org.yaen.starter.core.service.services.impl;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.integration.clients.WechatClient;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.entities.wechat.MessageEntity;
import org.yaen.starter.core.model.models.wechat.MessageModel;
import org.yaen.starter.core.model.models.wechat.enums.EventTypes;
import org.yaen.starter.core.model.models.wechat.enums.MessageTypes;
import org.yaen.starter.core.model.models.wechat.objects.AccessToken;
import org.yaen.starter.core.model.services.CacheService;
import org.yaen.starter.core.model.services.WechatService;
import org.yaen.starter.core.model.utils.WechatPropertiesUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * wechat service implement
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class WechatServiceImpl implements WechatService {
	/** cache key of access token */
	private static String CACHE_KEY_ACCESS_TOKEN = "WECHAT_ACCESS_TOKEN:";

	@Autowired
	private WechatClient wechatClient;

	@Autowired
	private CacheService cacheService;

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#checkSignature(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkSignature(String token, String signature, String timestamp, String nonce)
			throws NoSuchAlgorithmException {
		return wechatClient.checkSignature(token, signature, timestamp, nonce);
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#getAccessToken(java.lang.String)
	 */
	@Override
	public AccessToken getAccessToken(String appId) throws CoreException {

		// load default if not given
		if (StringUtil.isBlank(appId)) {
			appId = WechatPropertiesUtil.getAppId();
		}

		// get token from cache
		AccessToken accessToken = (AccessToken) this.cacheService.get(CACHE_KEY_ACCESS_TOKEN + appId);

		if (accessToken == null) {

			// secret
			// TODO get secret for different appid
			String secret = WechatPropertiesUtil.getSecret();

			// call client
			JSONObject jsonObject;
			try {
				jsonObject = JSONObject.parseObject(wechatClient.getAccessToken(appId, secret));
			} catch (Exception ex) {
				throw new CoreException("get access token error", ex);
			}

			// check result
			if (jsonObject == null) {
				throw new CoreException("wechat get access token failed");
			}

			// set token
			accessToken = new AccessToken();
			accessToken.setToken(jsonObject.getString("access_token"));
			accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));

			this.cacheService.set(CACHE_KEY_ACCESS_TOKEN + appId, accessToken, accessToken.getExpiresIn());
		}

		return accessToken;
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#pushMenu(java.lang.String, java.lang.String)
	 */
	@Override
	public void pushMenu(String menu, String appId) throws CoreException {

		// need access token
		AccessToken accessToken = this.getAccessToken(appId);

		// call client
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.parseObject(wechatClient.createMenu(menu, accessToken.getToken()));
		} catch (Exception ex) {
			throw new CoreException("wechat create menu failed", ex);
		}

		// check result
		if (jsonObject == null) {
			throw new CoreException("wechat create menu return null");
		} else {
			// check err code
			int errcode = jsonObject.getIntValue("errcode");
			if (errcode != 0) {
				throw new CoreException(
						"create menu failed, errcode=" + errcode + ", errmsg=" + jsonObject.getString("errmsg"));
			}
		}
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#isQqFace(java.lang.String)
	 */
	@Override
	public boolean isQqFace(String content) {
		boolean result = false;

		// 判断QQ表情的正则表达式
		String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
		Pattern p = Pattern.compile(qqfaceRegex);
		Matcher m = p.matcher(content);
		if (m.matches()) {
			result = true;
		}
		return result;
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#formatTime(java.lang.String)
	 */
	@Override
	public String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#makeResponse(org.yaen.starter.core.model.models.wechat.MessageModel)
	 */
	@Override
	public MessageModel makeResponse(MessageModel requestMessage) {
		AssertUtil.notNull(requestMessage);

		// create new message model
		MessageModel responseMessage = new MessageModel(requestMessage.getProxy(), requestMessage.getService());

		// response as text
		MessageEntity entity = responseMessage.getEntity();
		entity.setMsgType(MessageTypes.RESP_MESSAGE_TYPE_TEXT);
		entity.setToUserName(requestMessage.getEntity().getFromUserName());
		entity.setFromUserName(requestMessage.getEntity().getToUserName());
		entity.setCreateTime(DateUtil.getNow().getTime());

		String msgType = requestMessage.getEntity().getMsgType();
		String respContent = "";

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
			String eventType = requestMessage.getEntity().getEvent();
			String eventKey = requestMessage.getEntity().getEventKey();

			// 订阅
			if (eventType.equals(EventTypes.EVENT_TYPE_SUBSCRIBE)) {
				respContent = "谢谢您的关注！";
			}
			// 取消订阅
			else if (eventType.equals(EventTypes.EVENT_TYPE_UNSUBSCRIBE)) {
				// 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
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
		entity.setContent(respContent);

		return responseMessage;
	}

}
