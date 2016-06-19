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
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.integration.clients.WechatClient;
import org.yaen.starter.common.util.utils.PropertiesUtil;
import org.yaen.starter.core.model.models.wechat.AccessToken;
import org.yaen.starter.core.model.models.wechat.Article;
import org.yaen.starter.core.model.models.wechat.menus.Menu;
import org.yaen.starter.core.model.models.wechat.responses.MusicResponseMessage;
import org.yaen.starter.core.model.models.wechat.responses.NewsResponseMessage;
import org.yaen.starter.core.model.models.wechat.responses.TextResponseMessage;
import org.yaen.starter.core.model.services.WechatService;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * wechat service implement
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class WechatServiceImpl implements WechatService {

	@Autowired
	private WechatClient wechatClient;

	/**
	 * 扩展xstream，使其支持CDATA块
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
	 * get access token, the token will expire in 2 hours typically
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public AccessToken getAccessToken() throws Exception {

		// TODO check cache

		// the access token
		AccessToken accessToken = null;

		// get appid and secret
		String appid = PropertiesUtil.getProperty("wechat.appid");
		String secret = PropertiesUtil.getProperty("wechat.secret");

		// call client
		JSONObject jsonObject = wechatClient.getAccessToken(appid, secret);

		// check result
		if (jsonObject == null) {
			throw new CoreException("wechat get access token failed");
		} else {
			accessToken = new AccessToken();
			accessToken.setToken(jsonObject.getString("access_token"));
			accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
		}

		return accessToken;
	}

	/**
	 * create menu
	 * 
	 * @param menu
	 * @param accessToken
	 * @throws Exception
	 */
	@Override
	public void createMenu(Menu menu, AccessToken accessToken) throws Exception {

		// convert menu to json
		String jsonMenu = JSONObject.toJSONString(menu);

		// call client
		JSONObject jsonObject = wechatClient.createMenu(jsonMenu, accessToken.getToken());

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
	 * 解析微信发来的请求（xml）
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> parseXml(InputStream is) throws Exception {

		// 将解析结果存在Map中
		Map<String, String> map = new HashMap<String, String>();

		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(is);

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
	 * 回复的文本消息转换成xml格式
	 * 
	 * @param textMessage
	 * @return
	 */
	@Override
	public String textMessageToXml(TextResponseMessage textResponseMessage) {
		xStream.alias("xml", textResponseMessage.getClass());
		return xStream.toXML(textResponseMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 * 
	 * @param musicMessage
	 *            音乐消息对象
	 * @return xml
	 */
	@Override
	public String musicMessageToXml(MusicResponseMessage musicResponseMessage) {
		xStream.alias("xml", musicResponseMessage.getClass());
		return xStream.toXML(musicResponseMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsResponseMessage
	 *            图文消息对象
	 * @return xml
	 */
	@Override
	public String newsMessageToXml(NewsResponseMessage newsResponseMessage) {
		xStream.alias("xml", newsResponseMessage.getClass());
		xStream.alias("item", new Article().getClass());
		return xStream.toXML(newsResponseMessage);
	}

	/**
	 * 判断是否是QQ表情
	 * 
	 * @param content
	 * @return
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
	 * 将微信消息中的CreateTime转换成标准格式的时间（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param createTime
	 *            消息创建时间
	 * @return
	 */
	@Override
	public String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}

}
