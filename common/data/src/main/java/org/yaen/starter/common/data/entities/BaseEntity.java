/**
 * 
 */
package org.yaen.starter.common.data.entities;

import java.io.Serializable;

/**
 * the base entity with id
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
public interface BaseEntity extends Serializable {

	/**
	 * get the primary key of id
	 */
	public long getId();

	/**
	 * set the primary key of id
	 * 
	 * @param id
	 */
	public void setId(long id);

}
