/**
 * 
 */
package org.yaen.starter.common.data.models;

/**
 * fractal model, need parent, 1:1
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface BaseFractalModel extends BaseModel {

	/**
	 * get the base model
	 */
	public BaseModel getBase();

}
