package org.yaen.starter.web.home.utils;

import java.io.InputStream;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.WebException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.wechat.AccessToken;
import org.yaen.starter.core.model.models.wechat.Article;
import org.yaen.starter.core.model.models.wechat.menus.Menu;
import org.yaen.starter.core.model.models.wechat.responses.MusicResponseMessage;
import org.yaen.starter.core.model.models.wechat.responses.NewsResponseMessage;
import org.yaen.starter.core.model.models.wechat.responses.TextResponseMessage;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * wechat util for signatures and tokens and xml formating
 * 
 * @author Yaen 2016年6月6日下午10:17:52
 */
public class WechatUtil {

	// 获取access_token的接口地址（GET） 限200（次/天）
	/** get access token url (GET), limited to 200/day */
	public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	// 菜单创建（POST） 限100（次/天）
	/** create menu url (POST), limited to 100/day */
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	// 图文响应接口（POST） 限100（次/天）
	/** picture response url (POST), limited to 100/day */
	public final static String PIC_RESPONSE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	/**
	 * verify signature
	 * 
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean checkSignature(String token, String signature, String timestamp, String nonce)
			throws NoSuchAlgorithmException {
		AssertUtil.notBlank(token);
		AssertUtil.notBlank(signature);
		AssertUtil.notBlank(timestamp);
		AssertUtil.notBlank(nonce);

		// add token, timestamp, nonce to array, and sort
		String[] arr = new String[] { token, timestamp, nonce };
		Arrays.sort(arr);

		// combine together
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}

		// do sha-1
		MessageDigest md = MessageDigest.getInstance("SHA-1");

		byte[] digest = md.digest(content.toString().getBytes());
		String tmpStr = StringUtil.byteToStr(digest);

		// should be same
		return StringUtil.equals(tmpStr, signature.toUpperCase());
	}

	/**
	 * get access token, the token will expire in 2 hours typically
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	 * @throws Exception
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) throws Exception {

		AccessToken accessToken = null;

		// make url
		String url = ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);

		// do https get
		JSONObject jsonObject = HttpUtil.httpsGet(url);

		// check result
		if (jsonObject == null) {
			throw new WebException("https get failed, url=" + url);
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
	public static void createMenu(Menu menu, String accessToken) throws Exception {

		// make url
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);

		// convert menu to json string
		String jsonMenu = JSONObject.toJSONString(menu);

		// replace null, as we no not need it
		jsonMenu = jsonMenu.replaceAll(",null", "");

		// do https post
		JSONObject jsonObject = HttpUtil.httpsPost(url, jsonMenu);

		// check result
		if (jsonObject == null) {
			throw new WebException("https post failed, url=" + url);
		} else {
			// check err code
			int errcode = jsonObject.getIntValue("errcode");
			if (errcode != 0) {
				throw new BizException(
						"create menu failed, errcode=" + errcode + ", errmsg=" + jsonObject.getString("errmsg"));
			}
		}

		// here is all ok
	}

	/**
	 * 解析微信发来的请求（xml）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {

		// 将解析结果存在Map中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();

		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);

		// 取得xml的根元素
		Element root = document.getRootElement();

		// 得到根元素下所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		inputStream.close();
		inputStream = null;

		return map;

	}

	/**
	 * 回复的文本消息转换成xml格式
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextResponseMessage textResponseMessage) {
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
	public static String musicMessageToXml(MusicResponseMessage musicResponseMessage) {
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
	public static String newsMessageToXml(NewsResponseMessage newsResponseMessage) {
		xStream.alias("xml", newsResponseMessage.getClass());
		xStream.alias("item", new Article().getClass());
		return xStream.toXML(newsResponseMessage);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 */
	private static XStream xStream = new XStream(new XppDriver() {
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
	 * 判断是否是QQ表情
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isQqFace(String content) {
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
	public static String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}

}
