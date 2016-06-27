package org.yaen.starter.core.model.models;

import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Getter;

/**
 * one model as base model object
 * <p>
 * model is usually composed by entities and pojos and other model, and with logicals
 * <p>
 * model use service in inner logic, the caller do not need use service
 * <p>
 * one model can do one thing completely, and need nothing other
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public abstract class OneModel {

	/** the version of model, needed in interface call */
	@Getter
	private String version;

	/**
	 * protected constructor
	 * 
	 * @param version
	 */
	protected OneModel(String version) {
		
		// set version
		if (StringUtil.isNotBlank(version)) {
			this.version = version;
		} else {
			this.version = "1.0.0";
		}
	}
}
