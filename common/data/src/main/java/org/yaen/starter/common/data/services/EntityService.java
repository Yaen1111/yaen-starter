package org.yaen.starter.common.data.services;

import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.exceptions.CommonException;

/**
 * entity service for CRUD
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface EntityService {

	/**
	 * select entity by rowid
	 * 
	 * @param entity
	 * @param rowid
	 * @throws CommonException
	 */
	public <T extends BaseEntity> void selectEntityByRowid(T entity, long rowid) throws CommonException;

	/**
	 * insert entity, the rowid is auto-increase
	 * 
	 * @param entity
	 * @throws CommonException
	 */
	public <T extends BaseEntity> long insertEntityByRowid(T entity) throws CommonException;

	/**
	 * update entity by rowid, this id is also be updated
	 * 
	 * @param entity
	 * @throws CommonException
	 */
	public <T extends BaseEntity> void updateEntityByRowid(T entity) throws CommonException;

	/**
	 * delete entity by rowid
	 * 
	 * @param entity
	 * @throws CommonException
	 */
	public <T extends BaseEntity> void deleteEntityByRowid(T entity) throws CommonException;

	/**
	 * try select entity by rowid, return false if not exists
	 * 
	 * @param entity
	 * @param rowid
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> boolean trySelectEntityByRowid(T entity, long rowid) throws CommonException;

}
