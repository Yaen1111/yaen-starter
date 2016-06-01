package org.yaen.starter.core.model.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaen.starter.common.data.services.ModelService;

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
	private static ModelService service;

	/**
	 * none-static setter with autowired
	 * 
	 * @param modelService
	 */
	@Autowired
	public void setModelService(ModelService modelService) {
		ModelUtil.service = modelService;
	}
}
