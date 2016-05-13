/**
 * 
 */
package org.yaen.starter.common.data.models;

/**
 * attribute model. attribute need base, 1:N
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface AttributeModel extends BaseModel {

	/**
	 * get the base entity
	 */
	public BaseModel getBase();

}
