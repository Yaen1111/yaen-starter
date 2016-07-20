package org.yaen.starter.core.model.wechat.models;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.ComponentMessageEntity;
import org.yaen.starter.core.model.wechat.enums.InfoTypes;

import lombok.extern.slf4j.Slf4j;

/**
 * the wechat component message model, mostly for verify ticket and message
 * 
 * @author Yaen 2016年7月14日下午2:07:17
 */
@Slf4j
public class ComponentMessageModel extends TwoModel {

	@Override
	public ComponentMessageEntity getEntity() {
		return (ComponentMessageEntity) super.getEntity();
	}

	/**
	 * constructor
	 * 
	 * @param proxy
	 */
	public ComponentMessageModel(ProxyService proxy) {
		super(proxy, new ComponentMessageEntity());
	}

	/**
	 * parse from xml in input stream
	 * 
	 * @param is
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	public void loadFromXml(Reader reader) throws CoreException {
		AssertUtil.notNull(reader);

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
		log.debug("parse xml done, map={}", map);

		// get entity and fill data
		ComponentMessageEntity msg = this.getEntity();

		// main info
		msg.setAppid(map.get("AppId"));
		msg.setCreateTime(Long.parseLong(map.get("CreateTime")));
		msg.setInfoType(map.get("InfoType"));

		// make switch not null
		if (msg.getInfoType() == null) {
			msg.setInfoType("");
		}

		switch (msg.getInfoType()) {
		case InfoTypes.COMPONENT_VERIFY_TICKET:
			// ticket every 10 minutes
			msg.setComponentVerifyTicket(map.get("ComponentVerifyTicket"));
			break;
		case InfoTypes.AUTHORIZED:
		case InfoTypes.UPDATEAUTHORIZED:
			// authorized ok
			msg.setAuthorizerAppid(map.get("AuthorizerAppid"));
			msg.setComponentVerifyTicket(map.get("AuthorizationCode"));
			msg.setAuthorizationCodeExpiredTime(Long.parseLong(map.get("AuthorizationCodeExpiredTime")));
			break;
		case InfoTypes.UNAUTHORIZED:
			// authorize canceled/expired
			msg.setAuthorizerAppid(map.get("AuthorizerAppid"));
			break;
		default:
			log.error("unknown info type: {}", msg.getInfoType());
			break;
		}

		// done
	}

}
