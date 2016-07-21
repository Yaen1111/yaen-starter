package org.yaen.starter.core.model.services;

import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.integration.clients.HttpClient;

/**
 * proxy service for simple model implement
 * 
 * @author Yaen 2016年6月19日下午4:37:50
 */
public interface ProxyService {

	/**
	 * get proxy entity service
	 * 
	 * @return
	 */
	EntityService getEntityService();

	/**
	 * get proxy query service
	 * 
	 * @return
	 */
	QueryService getQueryService();

	/**
	 * get proxy http client
	 * 
	 * @return
	 */
	HttpClient getHttpClient();

	/**
	 * get proxy cache service
	 * 
	 * @return
	 */
	CacheService getCacheService();

}
