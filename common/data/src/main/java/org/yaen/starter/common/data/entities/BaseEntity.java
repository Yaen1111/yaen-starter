/**
 * 
 */
package org.yaen.starter.common.data.entities;

import java.io.Serializable;

import org.yaen.starter.common.data.annotations.Virtual;
import org.yaen.starter.common.data.services.EntityService;

/**
 * base entity
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface BaseEntity extends Cloneable, Serializable {

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

	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException;

	@Virtual
	public void BeforeSelect(EntityService service) throws Exception;

	@Virtual
	public void AfterSelect(EntityService service) throws Exception;

	@Virtual
	public void BeforeInsert(EntityService service) throws Exception;

	@Virtual
	public void AfterInsert(EntityService service) throws Exception;

	@Virtual
	public void BeforeUpdate(EntityService service) throws Exception;

	@Virtual
	public void AfterUpdate(EntityService service) throws Exception;

	@Virtual
	public void BeforeDelete(EntityService service) throws Exception;

	@Virtual
	public void AfterDelete(EntityService service) throws Exception;

}
