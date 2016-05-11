/**
 * 
 */
package org.yaen.starter.common.data.services;

import java.util.List;

import org.yaen.starter.common.data.entities.BaseEntity;

/**
 * entity service for CRUD
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface EntityService {

	/**
	 * select entity by id
	 * 
	 * @param entity
	 * @param id
	 * @throws Exception
	 */
	public <T extends BaseEntity> void selectEntity(T entity, long id) throws Exception;

	/**
	 * select entity list by given id list
	 * 
	 * @param entity
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseEntity> List<T> selectEntityList(T entity, List<Long> ids) throws Exception;

	/**
	 * insert entity
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public <T extends BaseEntity> long insertEntity(T entity) throws Exception;

	/**
	 * update entity
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public <T extends BaseEntity> void updateEntity(T entity) throws Exception;

	/**
	 * delete entity
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public <T extends BaseEntity> void deleteEntity(T entity) throws Exception;

}
