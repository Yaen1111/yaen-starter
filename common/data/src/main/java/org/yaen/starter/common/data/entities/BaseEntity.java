package org.yaen.starter.common.data.entities;

import java.io.Serializable;

/**
 * the base entity with rowid and id
 * <p>
 * rowid is the primary key, used for uid
 * <p>
 * id is the main key of data, used for s
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
public interface BaseEntity extends Serializable {

	/**
	 * get the primary key of rowid
	 */
	public long getRowid();

	/**
	 * set the primary key of rowid
	 * 
	 * @param rowid
	 */
	public void setRowid(long rowid);

	/**
	 * get the id
	 */
	public String getId();

	/**
	 * set the id
	 * 
	 * @param id
	 */
	public void setId(String id);

}
