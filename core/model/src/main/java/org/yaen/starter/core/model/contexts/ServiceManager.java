package org.yaen.starter.core.model.contexts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.integration.clients.WechatClient;

import lombok.Getter;

/**
 * service manager, used to get autowired service
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
@Component
public class ServiceManager {

	/** the static entity service */
	@Getter
	private static EntityService entityService;

	/** the static query service */
	@Getter
	private static QueryService queryService;

	/** the static wechat client */
	@Getter
	private static WechatClient wechatClient;

	/**
	 * none-static setter with autowired
	 * 
	 * @param entityService
	 */
	@Autowired
	public void setEntityService(EntityService entityService) {
		ServiceManager.entityService = entityService;
	}

	/**
	 * none-static setter with autowired
	 * 
	 * @param queryService
	 */
	@Autowired
	public void setQueryService(QueryService queryService) {
		ServiceManager.queryService = queryService;
	}

	/**
	 * none-static setter with autowired
	 * 
	 * @param wechatClient
	 */
	@Autowired
	public void setWechatClient(WechatClient wechatClient) {
		ServiceManager.wechatClient = wechatClient;
	}

}
