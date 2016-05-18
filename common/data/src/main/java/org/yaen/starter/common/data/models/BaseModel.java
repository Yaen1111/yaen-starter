/**
 * 
 */
package org.yaen.starter.common.data.models;

import java.io.Serializable;

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

	/**
	 * triggers, can be trigger chain
	 * 
	 * <pre>
	 * if (super.BeforeSelect) {
	 * 	// do something
	 * 	return true; // true if need next, false for all done
	 * } else {
	 * 	return false; // super already done, do nothing but return false
	 * }
	 * </pre>
	 * 
	 * @param service
	 * @return true for next chain, false for done
	 * @throws Exception
	 */
	public boolean BeforeSelect(ModelService service) throws Exception;

	public boolean AfterSelect(ModelService service) throws Exception;

	public boolean BeforeInsert(ModelService service) throws Exception;

	public boolean AfterInsert(ModelService service) throws Exception;

	public boolean BeforeUpdate(ModelService service) throws Exception;

	public boolean AfterUpdate(ModelService service) throws Exception;

	public boolean BeforeDelete(ModelService service) throws Exception;

	public boolean AfterDelete(ModelService service) throws Exception;

}
