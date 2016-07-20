package org.yaen.starter.core.service.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.integration.clients.HttpClient;
import org.yaen.starter.common.integration.clients.RedisClient;
import org.yaen.starter.core.model.services.ProxyService;

import lombok.Getter;

/**
 * proxy service impl
 * 
 * @author Yaen 2016年7月3日下午1:02:08
 */
@Service
public class ProxyServiceImpl implements ProxyService {

	@Getter
	@Autowired
	private EntityService entityService;

	@Getter
	@Autowired
	private QueryService queryService;

	@Getter
	@Autowired
	private HttpClient httpClient;

	@Getter
	@Autowired
	@Qualifier("generalCacheClient")
	private RedisClient redisClient;

}
