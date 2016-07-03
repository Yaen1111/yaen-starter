package org.yaen.starter.common.data.services;

import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.NoDataAffectedException;

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
	 * @throws DataNotExistsException
	 */
	public <T extends BaseEntity> void selectEntityByRowid(T entity, long rowid)
			throws CommonException, DataNotExistsException;

	/**
	 * insert entity, the rowid is auto-increase
	 * 
	 * @param entity
	 * @throws CommonException
	 * @throws NoDataAffectedException 
	 */
	public <T extends BaseEntity> long insertEntityByRowid(T entity) throws CommonException, NoDataAffectedException;

	/**
	 * update entity by rowid, this id is also be updated
	 * 
	 * @param entity
	 * @throws CommonException
	 * @throws NoDataAffectedException 
	 */
	public <T extends BaseEntity> void updateEntityByRowid(T entity) throws CommonException, NoDataAffectedException;

	/**
	 * delete entity by rowid
	 * 
	 * @param entity
	 * @throws CommonException
	 * @throws NoDataAffectedException 
	 */
	public <T extends BaseEntity> void deleteEntityByRowid(T entity) throws CommonException, NoDataAffectedException;

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
