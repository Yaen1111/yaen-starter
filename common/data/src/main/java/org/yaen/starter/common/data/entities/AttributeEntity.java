/**
 * 
 */
package org.yaen.starter.common.data.entities;

/**
 * attribute entity. attribute need base, 1:N
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface AttributeEntity extends BaseEntity {

	/**
	 * get the base entity
	 */
	public BaseEntity getBase();

}
