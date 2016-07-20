package org.yaen.starter.core.model.models;

import org.yaen.starter.common.data.exceptions.CoreException;

import lombok.Getter;

/**
 * one model as base model object
 * <p>
 * model is usually composed by entities and pojos and other model, and specialized service and logicals.
 * <p>
 * the service hold every logical, but use model only is recommended
 * <p>
 * change service implement may change model activities
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public abstract class OneModel {

	/** the version of model, can be used in interface call */
	@Getter
	protected String version = "1.0.0";

	/**
	 * check model status
	 * 
	 * @throws CoreException
	 */
	public abstract void check() throws CoreException;

}
