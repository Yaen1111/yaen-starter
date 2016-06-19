/**
 * 
 */
package org.yaen.starter.core.model.models;

import java.io.IOException;
import java.io.Serializable;

import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.exceptions.CoreException;

/**
 * base model, extends BaseEntity, with id and rowId
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface BaseModel extends BaseEntity, Cloneable, Serializable {

	/**
	 * get enable change log flag
	 * 
	 * @return
	 */
	public boolean isEnableChangeLog();

	/**
	 * set enable change log flag
	 * 
	 * @param saveChangeLog
	 */
	public void setEnableChangeLog(boolean enableChangeLog);

	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException;

	/**
	 * deep clone a object, just use serialize and deserialize
	 * 
	 * @return
	 */
	public Object deepClone() throws IOException;

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
	public boolean BeforeSelect() throws CoreException;

	public void AfterSelect() throws CoreException;

	public boolean BeforeInsert() throws CoreException;

	public void AfterInsert() throws CoreException;

	public boolean BeforeUpdate() throws CoreException;

	public void AfterUpdate() throws CoreException;

	public boolean BeforeDelete() throws CoreException;

	public void AfterDelete() throws CoreException;

}
