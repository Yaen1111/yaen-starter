package org.yaen.starter.common.data.services;

import java.util.List;

import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.objects.QueryBuilder;

/**
 * query service for most search operation
 * <p>
 * all result is id list, and then can page in application level
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface QueryService {

	/**
	 * select entity list by given rowid list, with triggers
	 * 
	 * @param entity
	 * @param rowids
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<T> selectListByRowids(T entity, List<Long> rowids) throws CommonException;

	/**
	 * select entity list by given id, with triggers
	 * 
	 * @param entity
	 * @param id
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<T> selectListById(T entity, String id) throws CommonException;

	/**
	 * select one entity by given id, with triggers, empty or duplicate will throw
	 * 
	 * @param entity
	 * @param id
	 * @return
	 * @throws CommonException
	 * @throws DataNotExistsException
	 * @throws DuplicateDataException
	 */
	public <T extends BaseEntity> T selectOneById(T entity, String id)
			throws CommonException, DataNotExistsException, DuplicateDataException;

	/**
	 * select value list by given id and field name, no triggers
	 * 
	 * @param entity
	 * @param id
	 * @param fieldName
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<Object> selectValueListById(T entity, String id, String fieldName)
			throws CommonException;

	/**
	 * select rowids by field name, the value is the given value in the entity and use equal, no triggers
	 * 
	 * @param entity
	 * @param fieldName
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<Long> selectRowidsByField(T entity, String fieldName) throws CommonException;

	/**
	 * select rowids by field name list, the value is the given value in the entity and use equal, no triggers
	 * 
	 * @param entity
	 * @param fieldNames
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<Long> selectRowidsByFields(T entity, String[] fieldNames) throws CommonException;

	/**
	 * select one entity by given field name, usually unique key, with triggers, empty or duplicate will throw
	 * 
	 * @param entity
	 * @param fieldNames
	 * @return
	 * @throws CommonException
	 * @throws DataNotExistsException
	 * @throws DuplicateDataException
	 */
	public <T extends BaseEntity> T selectOneByUniqueFields(T entity, String[] fieldNames)
			throws CommonException, DataNotExistsException, DuplicateDataException;

	/**
	 * select rowids by all none-null field, the value is the given value in the entity, no triggers
	 * 
	 * @param entity
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<Long> selectRowidsByAllField(T entity) throws CommonException;

	/**
	 * select rowids by given sql, starting with where, can include order, group, and having clause, no triggers
	 * 
	 * @param entity
	 * @param whereClause
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<Long> selectRowsByWhereClause(T entity, String whereClause)
			throws CommonException;

	/**
	 * select rowids by all, no triggers
	 * 
	 * @param entity
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<Long> selectRowidsByAll(T entity) throws CommonException;

	/**
	 * select rowids by query
	 * 
	 * @param entity
	 * @param queryBuilder
	 * @return
	 * @throws CommonException
	 */
	public <T extends BaseEntity> List<Long> selectRowidsByQuery(T entity, QueryBuilder queryBuilder)
			throws CommonException;

}
