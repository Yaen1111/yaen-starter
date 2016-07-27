package org.yaen.starter.core.model.wechat.models;

import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.objects.Article;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * the base message model, for platform and component, mostly for user request and server response
 * 
 * @author Yaen 2016年7月14日下午2:07:17
 */
@Slf4j
public abstract class BaseMessageModel extends TwoModel {

	/** the default appid of the message, just used for log */
	@Getter
	protected String appid;

	/**
	 * extend xstream to support cdata
	 */
	protected XStream xStream = new XStream(new XppDriver() {
		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {

				// the cdata flag
				boolean cdata = false;

				@Override
				public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
					// set cdata flag only for string type
					if (clazz.getName().equals(String.class.getName())) {
						this.cdata = true;
					} else {
						this.cdata = false;
					}

					// the name should be capital
					String name2 = name;

					if (name != null && name.length() >= 1 && !name.equals("xml") && !name.equals("item")) {
						name2 = name.substring(0, 1).toUpperCase() + name.substring(1);
					}

					super.startNode(name2, clazz);
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
	 * constructor for child
	 * 
	 * @param proxy
	 * @param service
	 * @param entity
	 */
	protected BaseMessageModel(ProxyService proxy, OneEntity entity) {
		super(proxy, entity);
	}

	/**
	 * private get map from xml reader
	 * 
	 * @param reader
	 * @return
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, String> getMapFormXml(Reader reader) throws CoreException {

		// make xml reader
		SAXReader sax_reader = new SAXReader();
		Document document;
		try {
			document = sax_reader.read(reader);
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
		if (log.isDebugEnabled()) {
			log.debug("parse xml done, map={}", map);
		}

		return map;
	}

	/**
	 * encrypt xml content
	 * <p>
	 * appid is the host appid(home platform for platform only, component for all binded platform), DO NOT use
	 * platform-component appid
	 * 
	 * @param xml
	 * @param appid
	 * @param token
	 * @param aesKey
	 * @return
	 * @throws AesException
	 */
	protected String encryptXml(String xml, String appid, String token, String aesKey) throws AesException {

		log.debug("encrypt xml content before decrypt: \n{}", xml);

		Date now = DateUtil.getNow();
		Long nowtime = now.getTime() / 1000;

		// make crypto
		WXBizMsgCrypt pc = new WXBizMsgCrypt(token, aesKey, appid);

		// encrypt
		String resp = pc.encryptMsg(xml, StringUtil.toString(nowtime),
				StringUtil.toString(Math.round(Math.random() * 1000000)));

		log.debug("encrypt xml content after decrypt: \n{}", resp);

		return resp;
	}

	/**
	 * decrypt xml
	 * <p>
	 * appid is the host appid(home platform for platform only, component for all binded platform), DO NOT use
	 * platform-component appid
	 * 
	 * @param xml
	 * @param appid
	 * @param token
	 * @param aesKey
	 * @param msgSignature
	 * @param timeStamp
	 * @param nonce
	 * @return
	 * @throws CoreException
	 * @throws AesException
	 */
	protected String decryptXml(String xml, String appid, String token, String aesKey, String msgSignature,
			String timeStamp, String nonce) throws CoreException, AesException {

		log.debug("decrypt xml content before decrypt: \n{}", xml);

		// make crypto
		WXBizMsgCrypt pc = new WXBizMsgCrypt(token, aesKey, appid);

		// decrypt entire xml
		String resp = pc.decryptMsg(msgSignature, timeStamp, nonce, xml);

		log.debug("decrypt xml content after decrypt: \n{}", resp);

		return resp;

	}

	/**
	 * do process message, need appid, as the message maybe send to component
	 * 
	 * @param appid
	 * @throws DataException
	 * @throws CommonException
	 * @throws CoreException
	 */
	public abstract void processMessage(String appid) throws DataException, CommonException, CoreException;

	/**
	 * make response message, return success if no message need to be response
	 * 
	 * @return
	 * @throws DataException
	 * @throws CommonException
	 * @throws CoreException
	 */
	public abstract String makeResponse() throws DataException, CommonException, CoreException;

	/**
	 * convert the message object to xml
	 * 
	 * @return
	 */
	public String toXml(Object msg) {
		AssertUtil.notNull(msg);

		// alias base xml
		xStream.alias("xml", msg.getClass());

		// alias item
		xStream.alias("item", Article.class);
		return xStream.toXML(msg);
	}

}
