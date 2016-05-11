/**
 * 
 */
package org.yaen.starter.common.data.entities;

/**
 * relation entity, need from entity and to entity, 1+1:N
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface RelationEntity extends BaseEntity {

	/**
	 * get the from entity
	 */
	public BaseEntity getFromEntity();

	/**
	 * get the to entity
	 */
	public BaseEntity getToEntity();
}
