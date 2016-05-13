/**
 * 
 */
package org.yaen.starter.common.data.models;

/**
 * relation model, need from entity and to entity, 1+1:N
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface RelationModel extends BaseModel {

	/**
	 * get the from entity
	 */
	public BaseModel getFromEntity();

	/**
	 * get the to entity
	 */
	public BaseModel getToEntity();
}
