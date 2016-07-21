package org.yaen.starter.core.model.wechat.models;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.ParseUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformMessageEntity;
import org.yaen.starter.core.model.wechat.enums.EventTypes;
import org.yaen.starter.core.model.wechat.enums.MessageTypes;
import org.yaen.starter.core.model.wechat.services.WechatService;

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
	 * @param service
	 * @param entity
	 */
	protected PlatformMessageModel(ProxyService proxy, WechatService service, PlatformMessageEntity entity) {
		super(proxy, service, entity);
	}

	/**
	 * constructor for self
	 * 
	 * @param proxy
	 * @param service
	 */
	public PlatformMessageModel(ProxyService proxy, WechatService service) {
		this(proxy, service, new PlatformMessageEntity());
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
	 * make response according to the content
	 * 
	 * @return
	 * @throws CoreException
	 */
	@Override
	public String makeResponse() throws CoreException {
		this.check();

		// call service, easy to inject different implement
		return this.service.makeResponse(this);
	}

}
