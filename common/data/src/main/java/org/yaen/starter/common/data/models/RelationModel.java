/**
 * 
 */
package org.yaen.starter.common.data.models;

/**
 * relation model, need from model and to model, 1+1:N
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface RelationModel extends BaseModel {

	/**
	 * get the from model
	 */
	public BaseModel getFromModel();

	/**
	 * get the to model
	 */
	public BaseModel getToModel();
}
