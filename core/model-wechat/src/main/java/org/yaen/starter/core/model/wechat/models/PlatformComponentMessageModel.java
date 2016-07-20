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
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformComponentMessageEntity;
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
public class PlatformComponentMessageModel extends PlatformMessageModel {

	@Override
	public PlatformComponentMessageEntity getEntity() {
		return (PlatformComponentMessageEntity) super.getEntity();
	}

	/** if has appid, this is for component-binded platform, otherwise is self */
	@Getter
	private String appid;

	/**
	 * @param proxy
	 * @param service
	 * @param appid
	 */
	public PlatformComponentMessageModel(ProxyService proxy, WechatService service, String appid) {
		super(proxy, service);

		this.appid = appid;
	}

}
