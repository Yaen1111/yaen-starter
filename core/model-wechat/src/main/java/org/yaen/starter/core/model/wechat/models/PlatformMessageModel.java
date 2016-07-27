package org.yaen.starter.core.model.wechat.models;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.ParseUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformMessageEntity;
import org.yaen.starter.core.model.wechat.entities.PlatformUserEntity;
import org.yaen.starter.core.model.wechat.enums.EventTypes;
import org.yaen.starter.core.model.wechat.enums.MessageTypes;
import org.yaen.starter.core.model.wechat.objects.TextResponseMessage;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * the wechat platform message model, mostly for user request and server response
 * 
 * @author Yaen 2016年7月14日下午2:07:17
 */
@Slf4j
public class PlatformMessageModel extends BaseMessageModel {

	@Override
	public PlatformMessageEntity getEntity() {
		return (PlatformMessageEntity) super.getEntity();
	}

	/**
	 * constructor for child
	 * 
	 * @param proxy
	 * @param entity
	 */
	protected PlatformMessageModel(ProxyService proxy, PlatformMessageEntity entity) {
		super(proxy, entity);

		// set default appid
		this.appid = WechatPropertiesUtil.getAppid();
	}

	/**
	 * constructor for self
	 * 
	 * @param proxy
	 */
	public PlatformMessageModel(ProxyService proxy) {
		this(proxy, new PlatformMessageEntity());
	}

	/**
	 * parse from xml in input stream
	 * 
	 * @param reader
	 * @throws CoreException
	 */
	public void loadFromXml(Reader reader) throws CoreException {
		AssertUtil.notNull(reader);

		// the value map
		Map<String, String> map = this.getMapFormXml(reader);

		// make entity according to the map item
		PlatformMessageEntity msg = this.getEntity();

		// main info
		msg.setAppid(this.appid);
		msg.setFromUserName(map.get("FromUserName"));
		msg.setToUserName(map.get("ToUserName"));
		msg.setCreateTime(ParseUtil.tryParseLong(map.get("CreateTime"), 0L));
		msg.setMsgType(map.get("MsgType"));

		// make switch not null
		if (msg.getMsgType() == null) {
			msg.setMsgType("");
		}

		if (StringUtil.equals(msg.getMsgType(), MessageTypes.REQ_MESSAGE_TYPE_EVENT)) {
			// is event, get event and eventkey(maybe null)
			msg.setEvent(map.get("Event"));
			msg.setEventKey(map.get("EventKey"));

			// make switch not null
			if (msg.getEvent() == null) {
				msg.setEvent("");
			}

			switch (msg.getEvent()) {
			case EventTypes.EVENT_TYPE_SUBSCRIBE:
			case EventTypes.EVENT_TYPE_SCAN:
				// SCAN may fire SUBSCRIBE, has Ticket info
				msg.setTicket(map.get("Ticket"));
				break;
			case EventTypes.EVENT_TYPE_UNSUBSCRIBE:
				// no more info
				break;
			case EventTypes.EVENT_TYPE_LOCATION:
				// location
				msg.setLatitude(new BigDecimal(map.get("Latitude")));
				msg.setLongitude(new BigDecimal(map.get("Longitude")));
				msg.setPrecision(new BigDecimal(map.get("Precision")));
				break;
			case EventTypes.EVENT_TYPE_CLICK:
				// click, eventkey=menuid
				break;
			case EventTypes.EVENT_TYPE_VIEW:
				// click link, eventkey=url
				break;
			case EventTypes.EVENT_TYPE_WIFI_CONNECTED:
				// wifi connected
				msg.setConnectTime(ParseUtil.tryParseLong(map.get("ConnectTime"), 0L));
				msg.setExpireTime(ParseUtil.tryParseLong(map.get("ExpireTime"), 0L));
				msg.setVendorId(map.get("VendorId"));
				msg.setShopId(map.get("ShopId"));
				msg.setDeviceNo(map.get("DeviceNo"));
				break;
			case EventTypes.EVENT_TYPE_QUALIFICATION_VERIFY_SUCCESS:
			case EventTypes.EVENT_TYPE_NAMING_VERIFY_SUCCESS:
			case EventTypes.EVENT_TYPE_ANNUAL_RENEW:
			case EventTypes.EVENT_TYPE_VERIFY_EXPIRED:
				// xxx verify success/anual renew/expired
				msg.setExpiredTime(ParseUtil.tryParseLong(map.get("ExpiredTime"), 0L));
				break;
			case EventTypes.EVENT_TYPE_QUALIFICATION_VERIFY_FAIL:
			case EventTypes.EVENT_TYPE_NAMING_VERIFY_FAIL:
				// xxx verify fail
				msg.setFailTime(ParseUtil.tryParseLong(map.get("FailTime"), 0L));
				msg.setFailReason(map.get("FailReason"));
				break;
			case EventTypes.EVENT_TYPE_POI_CHECK_NOTIFY:
				// poi(shop) check
				msg.setUniqId(map.get("UniqId"));
				msg.setPoiId(map.get("PoiId"));
				msg.setResult(map.get("Result"));
				msg.setMsg(map.get("msg"));
				break;
			case EventTypes.EVENT_TYPE_TEMPLATESENDJOBFINISH:
				// send template message job finish
				msg.setStatus(map.get("Status"));
				break;
			default:
				log.error("unknown event type: {}", msg.getEvent());
				break;
			}
		} else {
			// normal message
			msg.setMsgId(ParseUtil.tryParseLong(map.get("MsgId"), 0L));

			switch (msg.getMsgType()) {
			case MessageTypes.REQ_MESSAGE_TYPE_TEXT:
				// text
				msg.setContent(map.get("Content"));
				break;
			case MessageTypes.REQ_MESSAGE_TYPE_IMAGE:
				// image
				msg.setPicUrl(map.get("PicUrl"));
				msg.setMediaId(map.get("MediaId"));
				break;
			case MessageTypes.REQ_MESSAGE_TYPE_VOICE:
				// voice, may has recognition if enabled
				msg.setMediaId(map.get("MediaId"));
				msg.setFormat(map.get("Format"));
				msg.setRecognition(map.get("Recognition"));
				break;
			case MessageTypes.REQ_MESSAGE_TYPE_VIDEO:
			case MessageTypes.REQ_MESSAGE_TYPE_SHORTVIDEO:
				// video/shor video
				msg.setMediaId(map.get("MediaId"));
				msg.setThumbMediaId(map.get("ThumbMediaId"));
				break;
			case MessageTypes.REQ_MESSAGE_TYPE_LOCATION:
				// location
				msg.setLocationX(new BigDecimal(map.get("Location_X")));
				msg.setLocationY(new BigDecimal(map.get("Location_Y")));
				msg.setScale(ParseUtil.tryParseInt(map.get("Scale"), 0));
				msg.setLabel(map.get("Label"));
				break;
			case MessageTypes.REQ_MESSAGE_TYPE_LINK:
				// link
				msg.setTitle(map.get("Title"));
				msg.setDescription(map.get("Description"));
				msg.setUrl(map.get("Url"));
				break;
			default:
				log.error("unknown message type: {}", msg.getMsgType());
				break;
			}
		} // if is event

		// done
	}

	/**
	 * @see org.yaen.starter.core.model.wechat.models.BaseMessageModel#processMessage(java.lang.String)
	 */
	@Override
	public void processMessage(String appid) throws DataException, CommonException, CoreException {

		// save message and related
		PlatformMessageEntity msg = this.getEntity();
		PlatformUserModel puser = null;
		PlatformUserEntity user = null;

		Date now = DateUtil.getNow();
		Long nowtime = now.getTime() / 1000;

		if (StringUtil.isNotBlank(msg.getFromUserName())) {
			// has user info
			puser = new PlatformUserModel(this.proxy);

			// try get one, if not exists, create one
			puser.loadOrCreateByOpenId(msg.getFromUserName(), appid);

			// get user entity
			user = puser.getEntity();

			// set last active time
			user.setLastActiveTime(now);
		}

		// check event
		if (StringUtil.equals(msg.getMsgType(), MessageTypes.REQ_MESSAGE_TYPE_EVENT) && msg.getEvent() != null) {
			switch (msg.getEvent()) {
			case EventTypes.EVENT_TYPE_SUBSCRIBE:
				// user subscribe
				user.setSubscribe(1);
				user.setSubscribeTime(nowtime);
				break;
			case EventTypes.EVENT_TYPE_UNSUBSCRIBE:
				// user unsubscribe
				user.setSubscribe(0);
				user.setUnsubscribeTime(nowtime);
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
	 * @see org.yaen.starter.core.model.wechat.models.BaseMessageModel#makeResponse()
	 */
	@Override
	public String makeResponse() throws CoreException {

		PlatformMessageEntity msg = this.getEntity();

		Date now = DateUtil.getNow();
		Long nowtime = now.getTime() / 1000;

		// response as text
		TextResponseMessage resp = new TextResponseMessage();
		resp.setMsgType(MessageTypes.RESP_MESSAGE_TYPE_TEXT);
		resp.setToUserName(msg.getFromUserName());
		resp.setFromUserName(msg.getToUserName());
		resp.setCreateTime(nowtime);

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
		return this.toXml(resp);
	}

}
