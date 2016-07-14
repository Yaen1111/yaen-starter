package org.yaen.starter.core.model.models.wechat;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yaen.starter.common.dal.entities.wechat.MessageEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;

import lombok.Getter;

/**
 * the wechat message model, mostly for user request and server response
 * 
 * @author Yaen 2016年7月14日下午2:07:17
 */
public class MessageModel extends TwoModel<MessageEntity> {

	/** the message map */
	@Getter
	private Map<String, String> map;

	/**
	 * @param proxy
	 * @param sample
	 */
	public MessageModel(ProxyService proxy, MessageEntity sample) {
		super(proxy, sample);
	}

	/**
	 * parse from xml in input stream
	 * 
	 * @param is
	 * @throws CoreException
	 */
	public void parse(InputStream is) throws CoreException {
		AssertUtil.notNull(is);

		this.clear();

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

		// return map;

	}

}
