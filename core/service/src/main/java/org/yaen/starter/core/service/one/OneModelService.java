package org.yaen.starter.core.service.one;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.dal.mappers.OneMapper;
import org.yaen.starter.common.dal.mappers.ZeroMapper;
import org.yaen.starter.common.data.entities.AnotherEntity;
import org.yaen.starter.common.data.entities.MyDescribeEntity;
import org.yaen.starter.common.data.entities.OneColumnEntity;
import org.yaen.starter.common.data.entities.OneEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.enums.SqlTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsCoreException;
import org.yaen.starter.common.data.exceptions.OperationCancelledCoreException;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.log.Changelog;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * one model service for most operation
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class OneModelService implements ModelService {

	@Autowired
	private OneMapper oneMapper;

	@Autowired
	private ZeroMapper zeroMapper;

	/**
	 * create table if not exists, or alter table if columns differs, using given one entity
	 * 
	 * @throws Exception
	 */
	protected void CreateTable(OneEntity entity) throws Exception {

		List<MyDescribeEntity> describes = null;

		// describe table, if table not exists, throw exception
		try {
			describes = zeroMapper.describeTable(entity.getTableName());
		} catch (BadSqlGrammarException ex) {
			Throwable ex2 = ex.getCause();

			if (ex2 instanceof MySQLSyntaxErrorException
					&& StringUtil.like(((MySQLSyntaxErrorException) ex2).getSQLState(), "42S02")) {
				// maybe table not exists, catch and eat it
			} else {
				throw ex;
			}
		}

		// check table exists
		if (describes == null) {
			// not exists, create new table
			zeroMapper.createTable(entity);

		} else {

			// exists, try to check column type and missed column
			// loop every element, and add/mod if not suitable
			for (Entry<String, OneColumnEntity> entry : entity.getColumns().entrySet()) {
				boolean exists = false;

				for (MyDescribeEntity describe : describes) {
					if (StringUtil.like(describe.getMyField(), entry.getValue().getColumnName())) {

						exists = true;

						// has column, check type
						{
							String type = entry.getValue().getDataType();
							int size = entry.getValue().getDataSize();

							if (size == 0) {
								// some special type has default size
								if (StringUtil.like(type, DataTypes.BIGINT)) {
									size = 20;
								} else if (StringUtil.like(type, DataTypes.INT)) {
									size = 11;
								}
							}

							if (size > 0) {
								type = type + "(" + size + ")";
							}

							if (!StringUtil.like(type, describe.getMyType())) {
								// column type changed, try to modify
								entity.setModifiedFieldName(entry.getKey());
								zeroMapper.modifyColumn(entity);
							}
						}

						break;
					}
				} // for

				if (!exists) {
					// no column, try to add one
					entity.setAddedFieldName(entry.getKey());
					zeroMapper.addColumn(entity);
				}
			} // for
		} // describes == null

	}

	/**
	 * fill model by data in map, with rowid and id
	 * 
	 * @param model
	 * @param columns
	 * @param values
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	protected <T extends BaseModel> void fillModelByColumns(T model, Map<String, OneColumnEntity> columns,
			Map<String, Object> values) throws IllegalArgumentException, IllegalAccessException {

		// fill rowid and id
		if (values.containsKey("rowid")) {
			model.setRowid((Long) values.get("rowid"));
		}

		// fill model fields by value map
		for (Entry<String, OneColumnEntity> entry : columns.entrySet()) {
			OneColumnEntity info = entry.getValue();
			Field field = info.getField();

			// set value if exists
			if (values.containsKey(info.getColumnName())) {
				Object value = values.get(info.getColumnName());

				// set value of any type
				field.set(model, value);
			}
		}
	}

	/**
	 * inner select model by rowid, fill model fields, no triggers
	 * 
	 * @param model
	 * @param rowid
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	protected <T extends BaseModel> boolean innerSelectModelByRowid(T model, long rowid, OneEntity entity)
			throws CoreException {
		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// set rowid
			entity.setRowid(rowid);

			// call mapper
			Map<String, Object> map = oneMapper.selectByRowid(entity);

			// check existence
			if (map == null) {
				return false;
			}

			// fill field
			this.fillModelByColumns(model, entity.getColumns(), map);

			return true;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner insert, no triggers, no change log
	 * 
	 * @param model
	 * @param entity
	 * @throws CoreException
	 */
	protected <T extends BaseModel> void innerInsertModelByRowid(T model, OneEntity entity) throws CoreException {
		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// insert the given element
			int ret = oneMapper.insertByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new CoreException("insert failed");
			}

			// id already set into entity and bridged to model

		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner update, no trigger, no change log
	 * 
	 * @param model
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseModel> T innerUpdateModelByRowid(T model, OneEntity entity) throws CoreException {
		try {

			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// try get old one if need
			T old = null;

			if (model.isEnableChangeLog()) {
				// clone to old
				old = (T) model.clone();

				boolean exists = this.innerSelectModelByRowid(old, model.getRowid(), entity);

				if (!exists) {
					// not exists, throw
					throw new DataNotExistsCoreException("data for update not exists");
				}
			}

			// update the given element
			int ret = oneMapper.updateByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new CoreException("update failed");
			}

			return old;

		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner delete, no trigger, no change log
	 * 
	 * @param model
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseModel> T innerDeleteModel(T model, OneEntity entity) throws CoreException {
		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// try get old one if need
			T old = null;

			if (model.isEnableChangeLog()) {
				// clone to old
				old = (T) model.clone();

				boolean exists = this.innerSelectModelByRowid(old, model.getRowid(), entity);

				if (!exists) {
					// not exists, throw
					throw new DataNotExistsCoreException("data for delete not exists");
				}
			}

			// delete the given element
			int ret = oneMapper.deleteByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new CoreException("delete failed");
			}

			return old;

		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner select model list, no triggers, origin model is not changed
	 * 
	 * @param model
	 * @param rowids
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseModel> List<T> innerSelectModelListByRowids(T model, List<Long> rowids, OneEntity entity)
			throws CoreException {

		// return empty if rowids is empty
		if (rowids.isEmpty()) {
			return new ArrayList<T>();
		}

		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// set rowid list
			entity.setRowids(rowids);

			// call mapper
			List<Map<String, Object>> maps = oneMapper.selectListByRowids(entity);

			// check existence
			if (maps == null || maps.isEmpty()) {
				return new ArrayList<T>();
			}

			List<T> list = new ArrayList<T>(maps.size());

			// make models
			for (Map<String, Object> map : maps) {

				// create new model
				T newmodel = (T) model.getClass().newInstance();

				// fill field
				this.fillModelByColumns(newmodel, entity.getColumns(), map);

				list.add(newmodel);
			}
			return list;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner select model by id, no triggers, origin model is not changed
	 * 
	 * @param model
	 * @param rowids
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseModel> List<T> innerSelectModelListById(T model, String id, OneEntity entity)
			throws CoreException {
		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// set id
			entity.setId(id);

			// call mapper
			List<Map<String, Object>> maps = oneMapper.selectListById(entity);

			// check existence
			if (maps == null) {
				return null;
			}

			List<T> list = new ArrayList<T>(maps.size());

			// make models
			for (Map<String, Object> map : maps) {

				// create new model
				T newmodel = (T) model.getClass().newInstance();

				// fill field
				this.fillModelByColumns(newmodel, entity.getColumns(), map);

				list.add(newmodel);
			}
			return list;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner select value by id, no triggers, origin model is not changed
	 * 
	 * @param model
	 * @param rowids
	 * @param fieldName
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	protected <T extends BaseModel> List<Object> innerSelectValueListById(T model, String id, String fieldName,
			OneEntity entity) throws CoreException {
		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// set id and column
			entity.setId(id);
			entity.setSelectedColumnName(entity.getColumns().get(fieldName).getColumnName());

			// call mapper
			List<Object> values = oneMapper.selectValueListById(entity);

			return values;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#selectModelByRowid(org.yaen.starter.common.data.models.BaseModel,
	 *      long)
	 */
	@Override
	public <T extends BaseModel> void selectModelByRowid(T model, long rowid) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before select
		if (model.BeforeSelect()) {

			boolean exists = this.innerSelectModelByRowid(model, rowid, null);

			if (!exists) {
				// not exists
				throw new DataNotExistsCoreException("data not exists");
			}

			// trigger after select
			model.AfterSelect();

		} else {
			// select canceled
			throw new OperationCancelledCoreException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#insertModelByRowid(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> long insertModelByRowid(T model) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before insert
		if (model.BeforeInsert()) {

			// inner insert
			this.innerInsertModelByRowid(model, null);

			// save change log
			if (model.isEnableChangeLog()) {
				Changelog logmodel = new Changelog(SqlTypes.INSERT, null, model);
				logmodel.BeforeInsert();
				this.innerInsertModelByRowid(logmodel, null);
			}

			// id already set into entity and bridged to model

			// trigger after insert
			model.AfterInsert();
		}

		return model.getRowid();
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#updateModelByRowid(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> void updateModelByRowid(T model) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before update
		if (model.BeforeUpdate()) {

			// update model and return old one if need
			T old = this.innerUpdateModelByRowid(model, null);

			// save change log
			if (model.isEnableChangeLog()) {
				Changelog logmodel = new Changelog(SqlTypes.UPDATE, old, model);
				logmodel.BeforeInsert();
				this.innerInsertModelByRowid(logmodel, null);
			}

			// trigger after update
			model.AfterUpdate();
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#deleteModelByRowid(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> void deleteModelByRowid(T model) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before delete
		if (model.BeforeDelete()) {

			// try get old one
			T old = this.innerDeleteModel(model, null);

			// save change log
			if (model.isEnableChangeLog()) {
				Changelog logmodel = new Changelog(SqlTypes.DELETE, old, null);
				logmodel.BeforeInsert();
				this.innerInsertModelByRowid(logmodel, null);
			}

			// trigger after delete
			model.AfterDelete();
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#trySelectModelByRowid(org.yaen.starter.common.data.models.BaseModel,
	 *      long)
	 */
	@Override
	public <T extends BaseModel> boolean trySelectModelByRowid(T model, long id) throws CoreException {
		try {
			this.selectModelByRowid(model, id);
			return true;
		} catch (DataNotExistsCoreException ex) {
			return false;
		} catch (OperationCancelledCoreException ex) {
			return false;
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#selectModelListByRowids(org.yaen.starter.common.data.models.BaseModel,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseModel> List<T> selectModelListByRowids(T model, List<Long> rowids) throws CoreException {
		AssertUtil.notNull(model);
		AssertUtil.notNull(rowids);

		// trigger before select, only once is ok
		if (model.BeforeSelect()) {

			List<T> list = this.innerSelectModelListByRowids(model, rowids, null);

			// trigger after select each
			for (int i = 0; i < list.size(); i++) {
				list.get(i).AfterSelect();
			}

			return list;

		} else {
			// select canceled
			throw new OperationCancelledCoreException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#selectModelListById(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<T> selectModelListById(T model, String id) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before select, only once is ok
		if (model.BeforeSelect()) {

			List<T> list = this.innerSelectModelListById(model, id, null);

			// trigger after select each
			for (int i = 0; i < list.size(); i++) {
				list.get(i).AfterSelect();
			}

			return list;
		} else {
			// select canceled
			throw new OperationCancelledCoreException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#selectValueListById(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<Object> selectValueListById(T model, String id, String fieldName)
			throws CoreException {
		AssertUtil.notNull(model);
		AssertUtil.notEmpty(fieldName);

		// trigger before select, only once is ok
		if (model.BeforeSelect()) {

			List<Object> list = this.innerSelectValueListById(model, id, fieldName, null);

			return list;
		} else {
			// select canceled
			throw new OperationCancelledCoreException("select cancelled by trigger");
		}
	}

}
