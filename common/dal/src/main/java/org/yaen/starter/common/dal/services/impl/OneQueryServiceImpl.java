package org.yaen.starter.common.dal.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.dal.entities.OneColumnEntity;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.dal.entities.QueryEntity;
import org.yaen.starter.common.dal.mappers.QueryMapper;
import org.yaen.starter.common.dal.services.TableService;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.exceptions.DataOperationCancelledException;
import org.yaen.starter.common.data.objects.QueryBuilder;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;

/**
 * one event service for most operation
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class OneQueryServiceImpl implements QueryService {

	@Autowired
	private QueryMapper queryMapper;

	@Autowired
	private TableService tableService;

	/**
	 * inner select model list, no triggers, origin entity is not changed
	 * 
	 * @param entity
	 * @param rowids
	 * @return
	 * @throws CommonException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends OneEntity> List<T> innerSelectListByRowids(T entity, List<Long> rowids)
			throws CommonException {

		List<T> list = new ArrayList<T>();

		// return empty if rowids is empty
		if (rowids.isEmpty()) {
			return list;
		}

		try {

			// create table if not exists
			tableService.CreateTable(entity);

			// set rowid list
			entity.setRowids(rowids);

			// call mapper
			List<Map<String, Object>> maps = queryMapper.selectListByRowids(entity);

			// check existence
			if (maps == null || maps.isEmpty()) {
				return list;
			}

			// make entities
			for (Map<String, Object> map : maps) {

				// create new entity
				T newentity = (T) entity.getClass().newInstance();

				// fill field
				newentity.fillValues(map);

				list.add(newentity);
			}
			return list;

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * inner select model by id, no triggers, origin entity is not changed
	 * 
	 * @param entity
	 * @param id
	 * @return
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends OneEntity> List<T> innerSelectListById(T entity, String id) throws CommonException {

		List<T> list = new ArrayList<T>();

		try {

			// create table if not exists
			tableService.CreateTable(entity);

			// set id
			entity.setId(id);

			// call mapper
			List<Map<String, Object>> maps = queryMapper.selectListById(entity);

			// check existence
			if (maps == null) {
				return list;
			}

			// make models
			for (Map<String, Object> map : maps) {

				// create new model
				T newmodel = (T) entity.getClass().newInstance();

				// fill field
				entity.fillValues(map);

				list.add(newmodel);
			}
			return list;

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * inner select value by id, no triggers, origin entity is not changed
	 * 
	 * @param entity
	 * @param rowids
	 * @param fieldName
	 * @return
	 * @throws CoreException
	 */
	protected <T extends OneEntity> List<Object> innerSelectValueListById(T entity, String id, String fieldName)
			throws CommonException {
		try {

			// create table if not exists
			tableService.CreateTable(entity);

			// set id and column
			entity.setId(id);
			entity.setSelectedColumnName(entity.getColumns().get(fieldName).getColumnName());

			// call mapper
			List<Object> values = queryMapper.selectValueListById(entity);

			return values;

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectListByRowids(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> List<T> selectListByRowids(T entity, List<Long> rowids)
			throws CommonException, DataOperationCancelledException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");
		AssertUtil.notNull(rowids);

		OneEntity one = (OneEntity) entity;

		// trigger before select, only once is ok
		if (one.BeforeSelect()) {

			List<OneEntity> listone = this.innerSelectListByRowids(one, rowids);

			List<T> list = new ArrayList<T>(listone.size());

			// trigger after select each
			for (OneEntity e : listone) {
				e.AfterSelect();
				list.add((T) e);
			}

			return list;

		} else {
			// select canceled
			throw new DataOperationCancelledException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectListById(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> List<T> selectListById(T entity, String id)
			throws CommonException, DataOperationCancelledException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		OneEntity one = (OneEntity) entity;

		// trigger before select, only once is ok
		if (one.BeforeSelect()) {

			// convert and check each entity
			List<OneEntity> listone = this.innerSelectListById(one, id);

			List<T> list = new ArrayList<T>(listone.size());

			// trigger after select each
			for (OneEntity e : listone) {
				e.AfterSelect();
				list.add((T) e);
			}

			return list;
		} else {
			// select canceled
			throw new DataOperationCancelledException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectOneById(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseEntity> T selectOneById(T entity, String id)
			throws CommonException, DataNotExistsException, DuplicateDataException, DataOperationCancelledException {
		List<T> list = this.selectListById(entity, id);

		// check empty
		if (list == null || list.isEmpty()) {
			throw new DataNotExistsException("data not exists, id=" + id);
		}

		// check duplicate
		if (list.size() > 1) {
			throw new DuplicateDataException("data id duplicate, id=" + id);
		}

		return list.get(0);
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectValueListById(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public <T extends BaseEntity> List<Object> selectValueListById(T entity, String id, String fieldName)
			throws CommonException, DataOperationCancelledException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");
		AssertUtil.notBlank(fieldName);

		OneEntity one = (OneEntity) entity;

		// trigger before select, only once is ok
		if (one.BeforeSelect()) {

			List<Object> list = this.innerSelectValueListById(one, id, fieldName);

			return list;
		} else {
			// select canceled
			throw new DataOperationCancelledException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByField(org.yaen.starter.core.model.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseEntity> List<Long> selectRowidsByField(T entity, String fieldName) throws CommonException {
		return this.selectRowidsByFields(entity, new String[] { fieldName });
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByFields(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.lang.String[])
	 */
	@Override
	public <T extends BaseEntity> List<Long> selectRowidsByFields(T entity, String[] fieldNames)
			throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");
		AssertUtil.notNull(fieldNames);

		OneEntity one = (OneEntity) entity;

		try {
			// create table if not exists
			tableService.CreateTable(one);

			// make event model
			QueryEntity query = new QueryEntity();

			// set table name
			query.setTableName(one.getTableName());
			query.setRowkey(one.getRowkey());
			
			// make columns
			{
				Map<String, Object> query_columns = new HashMap<String, Object>();

				Map<String, OneColumnEntity> columns = one.getColumns();

				for (String fieldname : fieldNames) {

					if (columns.containsKey(fieldname)) {
						OneColumnEntity info = columns.get(fieldname);
						query_columns.put(info.getColumnName(), info.getValue());
					}
				}

				query.setColumns(query_columns);
			}

			// call mapper
			return queryMapper.selectRowidsByColumns(query);

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectOneByUniqueFields(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.lang.String[])
	 */
	@Override
	public <T extends BaseEntity> T selectOneByUniqueFields(T entity, String[] fieldNames)
			throws CommonException, DataNotExistsException, DuplicateDataException, DataOperationCancelledException {
		AssertUtil.notNull(entity);

		List<Long> rowids = this.selectRowidsByFields(entity, fieldNames);

		// check empty
		if (rowids == null || rowids.isEmpty()) {
			throw new DataNotExistsException("data not exists, fieldNames=" + fieldNames);
		}

		// check duplicate
		if (rowids.size() > 1) {
			throw new DuplicateDataException("data id duplicate, fieldNames=" + fieldNames);
		}

		// get entity
		List<Long> oneid = new ArrayList<Long>(1);
		oneid.add(rowids.get(0));
		List<T> list = this.selectListByRowids(entity, oneid);

		// check empty again for concurrent
		if (list == null || list.isEmpty()) {
			throw new DataNotExistsException("data not exists, fieldNames=" + fieldNames);
		}

		// check duplicate
		if (list.size() > 1) {
			throw new DuplicateDataException("data id duplicate, fieldNames=" + fieldNames);
		}

		return list.get(0);
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByAllField(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> List<Long> selectRowidsByAllField(T entity) throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		OneEntity one = (OneEntity) entity;

		try {
			// create table if not exists
			tableService.CreateTable(one);

			// make event model
			QueryEntity query = new QueryEntity();

			// set table name
			query.setTableName(one.getTableName());
			query.setRowkey(one.getRowkey());
			
			// make columns
			{
				Map<String, Object> query_columns = new HashMap<String, Object>();

				Map<String, OneColumnEntity> columns = one.getColumns();

				// add columns if not id and is not null
				for (Entry<String, OneColumnEntity> entry : columns.entrySet()) {
					if (!StringUtil.like(entry.getKey(), "id") && entry.getValue().getValue() != null) {
						query_columns.put(entry.getValue().getColumnName(), entry.getValue().getValue());
					}
				}

				query.setColumns(query_columns);
			}

			// call mapper
			return queryMapper.selectRowidsByColumns(query);

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowsByWhereClause(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseEntity> List<Long> selectRowsByWhereClause(T entity, String whereClause)
			throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");
		AssertUtil.notNull(whereClause);

		OneEntity one = (OneEntity) entity;

		try {
			// create table if not exists
			tableService.CreateTable(one);

			// make event model
			QueryEntity query = new QueryEntity();
			
			// set table name
			query.setTableName(one.getTableName());
			query.setRowkey(one.getRowkey());

			// set where clause
			query.setWhereClause(whereClause);

			// call mapper
			return queryMapper.selectRowidsByWhereClause(query);

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByAll(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> List<Long> selectRowidsByAll(T entity) throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		OneEntity one = (OneEntity) entity;

		try {
			// create table if not exists
			tableService.CreateTable(one);

			// make event model
			QueryEntity query = new QueryEntity();

			// set table name
			query.setTableName(one.getTableName());
			query.setRowkey(one.getRowkey());

			// call mapper
			return queryMapper.selectRowidsByAll(query);

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByQuery(org.yaen.starter.common.data.entities.BaseEntity,
	 *      org.yaen.starter.common.data.objects.QueryBuilder)
	 */
	@Override
	public <T extends BaseEntity> List<Long> selectRowidsByQuery(T entity, QueryBuilder queryBuilder)
			throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		OneEntity one = (OneEntity) entity;

		try {
			// create table if not exists
			tableService.CreateTable(one);

			// make event model
			QueryEntity query = new QueryEntity();

			// set table name
			query.setTableName(one.getTableName());
			query.setRowkey(one.getRowkey());
			
			// set where if not empty
			if (!queryBuilder.getWhereEquals().isEmpty()) {
				throw new CommonException("not implemented");
				// TODO
			}

			// set order if not empty
			if (!queryBuilder.getOrders().isEmpty()) {
				throw new CommonException("not implemented");
				// TODO
			}

			// set pager if not empty
			if (queryBuilder.getPager().getItemPerPage() > 0) {
				throw new CommonException("not implemented");
				// TODO
			}

			// call mapper
			return queryMapper.selectRowidsByAll(query);

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

}
