package org.yaen.starter.core.model.contexts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaen.starter.core.model.services.ModelService;
import org.yaen.starter.core.model.services.QueryService;

import lombok.Getter;

/**
 * model context, used to get autowired service
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
@Component
public class ModelLoader {

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
		ModelLoader.modelService = modelService;
	}

	/**
	 * none-static setter with autowired
	 * 
	 * @param modelService
	 */
	@Autowired
	public void setQueryService(QueryService queryService) {
		ModelLoader.queryService = queryService;
	}
}
