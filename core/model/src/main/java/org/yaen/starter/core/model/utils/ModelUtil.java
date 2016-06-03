package org.yaen.starter.core.model.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.data.services.QueryService;

import lombok.Getter;

/**
 * model util, used to get autowired service
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
@Component
public class ModelUtil {

	/** the static model service */
	@Getter
	private static ModelService modelService;

	/** the static query service */
	@Getter
	private static QueryService queryService;

	/**
	 * none-static setter with autowired
	 * 
	 * @param modelService
	 */
	@Autowired
	public void setModelService(ModelService modelService) {
		ModelUtil.modelService = modelService;
	}

	/**
	 * none-static setter with autowired
	 * 
	 * @param modelService
	 */
	@Autowired
	public void setQueryService(QueryService queryService) {
		ModelUtil.queryService = queryService;
	}
}
