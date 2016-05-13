/**
 * 
 */
package org.yaen.starter.common.data.models;

import java.io.Serializable;

import org.yaen.starter.common.data.annotations.Virtual;
import org.yaen.starter.common.data.services.ModelService;

/**
 * base model
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface BaseModel extends Cloneable, Serializable {

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
	public void BeforeSelect(ModelService service) throws Exception;

	@Virtual
	public void AfterSelect(ModelService service) throws Exception;

	@Virtual
	public void BeforeInsert(ModelService service) throws Exception;

	@Virtual
	public void AfterInsert(ModelService service) throws Exception;

	@Virtual
	public void BeforeUpdate(ModelService service) throws Exception;

	@Virtual
	public void AfterUpdate(ModelService service) throws Exception;

	@Virtual
	public void BeforeDelete(ModelService service) throws Exception;

	@Virtual
	public void AfterDelete(ModelService service) throws Exception;

}
