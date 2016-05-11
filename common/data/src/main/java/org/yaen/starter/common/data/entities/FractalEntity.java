/**
 * 
 */
package org.yaen.starter.common.data.entities;

/**
 * fractal entity, need parent, 1:1
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface FractalEntity extends BaseEntity {

	/**
	 * get the parent entity
	 */
	public BaseEntity getParent();

}
