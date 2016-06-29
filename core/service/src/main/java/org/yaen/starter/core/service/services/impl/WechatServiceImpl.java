package org.yaen.starter.core.service.services.impl;

import java.io.InputStream;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.dal.entities.wechat.MenuEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.objects.NameValue;
import org.yaen.starter.common.data.objects.NameValueT;
import org.yaen.starter.common.data.objects.QueryBuilder;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.integration.clients.WechatClient;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.PropertiesUtil;
import org.yaen.starter.core.model.models.wechat.enums.EventTypes;
import org.yaen.starter.core.model.models.wechat.enums.MessageTypes;
import org.yaen.starter.core.model.models.wechat.objects.AccessToken;
import org.yaen.starter.core.model.models.wechat.objects.Article;
import org.yaen.starter.core.model.models.wechat.objects.MusicResponseMessage;
import org.yaen.starter.core.model.models.wechat.objects.NewsResponseMessage;
import org.yaen.starter.core.model.models.wechat.objects.TextResponseMessage;
import org.yaen.starter.core.model.services.WechatService;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import lombok.extern.slf4j.Slf4j;

/**
 * wechat service implement
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Slf4j
@Service
public class WechatServiceImpl implements WechatService {
	/** the cached access token */
	private AccessToken m_accessToken;

	/** the cache time */
	private long m_accessTokenTimeStamp;

	@Autowired
	private WechatClient wechatClient;

	@Autowired
	private QueryService queryService;

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
	 * @see org.yaen.starter.core.model.services.WechatService#checkSignature(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkSignature(String token, String signature, String timestamp, String nonce)
			throws NoSuchAlgorithmException {
		return wechatClient.checkSignature(token, signature, timestamp, nonce);
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#getAccessToken()
	 */
	@Override
	public AccessToken getAccessToken() throws CoreException {

		// check cache
		// TODO use redis
		long now = DateUtil.getNow().getTime();
		if (m_accessToken == null || m_accessTokenTimeStamp + 1000 <= now) {

			// get appid and secret
			String appid = PropertiesUtil.getProperty("wechat.appid");
			String secret = PropertiesUtil.getProperty("wechat.secret");

			// call client
			JSONObject jsonObject;
			try {
				jsonObject = wechatClient.getAccessToken(appid, secret);
			} catch (Exception ex) {
				throw new CoreException("get access token error", ex);
			}

			// check result
			if (jsonObject == null) {
				throw new CoreException("wechat get access token failed");
			} else {
				m_accessToken = new AccessToken();
				m_accessToken.setToken(jsonObject.getString("access_token"));
				m_accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
			}

			// set update time
			m_accessTokenTimeStamp = now;

		}

		return m_accessToken;
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#createMenu(java.lang.String,
	 *      org.yaen.starter.core.model.models.wechat.objects.AccessToken)
	 */
	@Override
	public void createMenu(String menu, AccessToken accessToken) throws CoreException {
		AssertUtil.notNull(menu);
		AssertUtil.notNull(accessToken);

		// call client
		JSONObject jsonObject;
		try {
			jsonObject = wechatClient.createMenu(menu, accessToken.getToken());
		} catch (Exception ex) {
			throw new CoreException("wechat create menu failed", ex);
		}

		// check result
		if (jsonObject == null) {
			throw new CoreException("wechat create menu failed");
		} else {
			// check err code
			int errcode = jsonObject.getIntValue("errcode");
			if (errcode != 0) {
				throw new CoreException(
						"create menu failed, errcode=" + errcode + ", errmsg=" + jsonObject.getString("errmsg"));
			}
		}

		// here is all ok
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#getMenuEntityList(java.lang.String)
	 */
	@Override
	public List<MenuEntity> getMenuEntityList(String groupName) throws CoreException {

		// get all menu
		MenuEntity entity = new MenuEntity();

		List<MenuEntity> list = null;

		try {
			QueryBuilder qb = new QueryBuilder();
			qb.getWhereEquals().add(new NameValue("groupName", groupName));
			qb.getOrders().add(new NameValueT<Boolean>("orders", true));

			List<Long> rowids = queryService.selectRowidsByQuery(entity, qb);
			list = queryService.selectListByRowids(entity, rowids);
		} catch (CommonException ex) {
			throw new CoreException("get menu entity error", ex);
		}

		return list;
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#parseXml(java.io.InputStream)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> parseXml(InputStream is) throws CoreException {
		AssertUtil.notNull(is);

		// 将解析结果存在Map中
		Map<String, String> map = new HashMap<String, String>();

		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(is);
		} catch (DocumentException ex) {
			throw new CoreException("read xml from is error", ex);
		}

		// 取得xml的根元素
		Element root = document.getRootElement();

		// 得到根元素下所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		return map;
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#textMessageToXml(org.yaen.starter.core.model.models.wechat.objects.TextResponseMessage)
	 */
	@Override
	public String textMessageToXml(TextResponseMessage textResponseMessage) {
		AssertUtil.notNull(textResponseMessage);

		xStream.alias("xml", textResponseMessage.getClass());
		return xStream.toXML(textResponseMessage);
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#musicMessageToXml(org.yaen.starter.core.model.models.wechat.objects.MusicResponseMessage)
	 */
	@Override
	public String musicMessageToXml(MusicResponseMessage musicResponseMessage) {
		AssertUtil.notNull(musicResponseMessage);

		xStream.alias("xml", musicResponseMessage.getClass());
		return xStream.toXML(musicResponseMessage);
	}

	/**
	 * @see org.yaen.starter.core.model.services.WechatService#newsMessageToXml(org.yaen.starter.core.model.models.wechat.objects.NewsResponseMessage)
	 */
	@Override
	public String newsMessageToXml(NewsResponseMessage newsResponseMessage) {
		AssertUtil.notNull(newsResponseMessage);

		xStream.alias("xml", newsResponseMessage.getClass());
		xStream.alias("item", new Article().getClass());
		return xStream.toXML(newsResponseMessage);
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
	 * @see org.yaen.starter.core.model.services.WechatService#handleRequest(java.util.Map)
	 */
	@Override
	public String handleRequest(Map<String, String> requestMap) {
		AssertUtil.notNull(requestMap);
		AssertUtil.notEmpty(requestMap);

		String respMessage = "";

		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";

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
					// 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(EventTypes.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");

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

			textResponseMessage.setContent(respContent);
			respMessage = this.textMessageToXml(textResponseMessage);
		} catch (Exception ex) {
			log.error("wechat servlet error:", ex);
		}

		return respMessage;
	}

}
