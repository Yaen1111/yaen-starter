package org.yaen.starter.core.model.wechat.models;

import java.io.InputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformMessageEntity;
import org.yaen.starter.core.model.wechat.enums.EventTypes;
import org.yaen.starter.core.model.wechat.enums.MessageTypes;
import org.yaen.starter.core.model.wechat.services.WechatService;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * the wechat platform message model, mostly for user request and server response
 * 
 * @author Yaen 2016年7月14日下午2:07:17
 */
@Slf4j
public class PlatformMessageModel extends TwoModel {

	/** the typed entity, overrides default entity */
	@Getter
	private PlatformMessageEntity entity;

	@Override
	public OneEntity getDefaultEntity() {
		return this.entity;
	}

	@Override
	public void setDefaultEntity(OneEntity defaultEntity) {
		this.entity = (PlatformMessageEntity) defaultEntity;
		super.setDefaultEntity(defaultEntity);
	}

	/** if has appid, this is for component-binded platform, otherwise is self */
	@Getter
	private String appid;

	@Getter
	private WechatService service;

	/**
	 * extend xstream to support cdata
	 */
	private XStream xStream = new XStream(new XppDriver() {
		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@Override
				public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
				}

				@Override
				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	/**
	 * constructor for self
	 * 
	 * @param proxy
	 * @param service
	 */
	public PlatformMessageModel(ProxyService proxy, WechatService service) {
		super(proxy, new PlatformMessageEntity());

		this.service = service;
	}

	/**
	 * constructor for component-binded platform
	 * 
	 * @param proxy
	 * @param service
	 */
	public PlatformMessageModel(ProxyService proxy, WechatService service, String appid) {
		this(proxy, service);

		this.appid = appid;
	}

	/**
	 * parse from xml in input stream
	 * 
	 * @param is
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	public void loadFromXml(InputStream is) throws CoreException {
		AssertUtil.notNull(is);

		this.clear();

		// make xml reader
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(is);
		} catch (DocumentException ex) {
			throw new CoreException("read xml from inputstream error", ex);
		}

		// get root
		Element root = document.getRootElement();

		// get all elements
		List<Element> elementList = (List<Element>) root.elements();

		// the value map
		Map<String, String> map = new HashMap<String, String>();

		// add all elements to map
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		// output map
		log.debug("parse xml done, map={}", map);

		// make entity according to the map item
		PlatformMessageEntity msg = new PlatformMessageEntity();

		// main info
		msg.setFromUserName(map.get("FromUserName"));
		msg.setToUserName(map.get("ToUserName"));
		msg.setCreateTime(Long.parseLong(map.get("CreateTime")));
		msg.setMsgType(map.get("MsgType"));

		if (StringUtil.equals(msg.getMsgType(), MessageTypes.REQ_MESSAGE_TYPE_EVENT)) {
			// is event, get event and eventkey(maybe null)
			msg.setEvent(map.get("Event"));
			msg.setEventKey(map.get("EventKey"));

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
				msg.setConnectTime(Long.parseLong(map.get("ConnectTime")));
				msg.setExpireTime(Long.parseLong(map.get("ExpireTime")));
				msg.setVendorId(map.get("VendorId"));
				msg.setShopId(map.get("ShopId"));
				msg.setDeviceNo(map.get("DeviceNo"));
				break;
			case EventTypes.EVENT_TYPE_QUALIFICATION_VERIFY_SUCCESS:
			case EventTypes.EVENT_TYPE_NAMING_VERIFY_SUCCESS:
			case EventTypes.EVENT_TYPE_ANNUAL_RENEW:
			case EventTypes.EVENT_TYPE_VERIFY_EXPIRED:
				// xxx verify success/anual renew/expired
				msg.setExpiredTime(Long.parseLong(map.get("ExpiredTime")));
				break;
			case EventTypes.EVENT_TYPE_QUALIFICATION_VERIFY_FAIL:
			case EventTypes.EVENT_TYPE_NAMING_VERIFY_FAIL:
				// xxx verify fail
				msg.setFailTime(Long.parseLong(map.get("FailTime")));
				msg.setFailReason(map.get("FailReason"));
				break;
			case EventTypes.EVENT_TYPE_POI_CHECK_NOTIFY:
				// poi(shop) check
				msg.setUniqId(map.get("UniqId"));
				msg.setPoiId(map.get("PoiId"));
				msg.setResult(map.get("Result"));
				msg.setMsg(map.get("msg"));
				break;
			default:
				log.error("unknown event type: {}", msg.getEvent());
				break;
			}
		} else {
			// normal message
			msg.setMsgId(Long.parseLong(map.get("MsgId")));

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
				msg.setScale(Integer.parseInt(map.get("Scale")));
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

		// done and copy
		this.entity = msg;
	}

	/**
	 * make response according to the content
	 * 
	 * @return
	 * @throws CoreException
	 */
	public PlatformMessageModel makeResponse() throws CoreException {
		this.check();

		// call service, easy to inject different implement
		return this.service.makeResponse(this);
	}

	/**
	 * convert the message to xml
	 * 
	 * @return
	 * @throws CoreException
	 */
	public String toXml() throws CoreException {
		this.check();

		// TODO make xml for all none-null fields in entity
		xStream.alias("xml", this.getEntity().getClass());
		return xStream.toXML(this.getEntity());
	}

}
