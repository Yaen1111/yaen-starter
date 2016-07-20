package org.yaen.starter.core.model.wechat.models;

import java.io.Reader;
import java.io.Writer;
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
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.services.WechatService;

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

	@Getter
	protected WechatService service;

	/**
	 * extend xstream to support cdata
	 */
	protected XStream xStream = new XStream(new XppDriver() {
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
	 * constructor for child
	 * 
	 * @param proxy
	 * @param service
	 * @param entity
	 */
	protected BaseMessageModel(ProxyService proxy, WechatService service, OneEntity entity) {
		super(proxy, entity);

		this.service = service;
	}

	/**
	 * get map from xml reader
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
	 * make response according to the content
	 * 
	 * @return
	 * @throws CoreException
	 */
	public abstract String makeResponse() throws CoreException;

	/**
	 * convert the message object to xml
	 * 
	 * @return
	 */
	public String toXml(Object msg) {
		AssertUtil.notNull(msg);
		xStream.alias("xml", msg.getClass());
		return xStream.toXML(msg);
	}

}
