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
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.OperationCancelledException;
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
	 * inner select model, no triggers
	 * 
	 * @param model
	 * @param id
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	protected <T extends BaseModel> boolean innerSelectModel(T model, long id, OneEntity entity) throws CoreException {

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
			Map<String, Object> map = oneMapper.selectByID(entity);

			// check existence
			if (map == null) {
				return false;
			}

			// set id again
			model.setId(id);

			// map column to field
			Map<String, OneColumnEntity> columns = entity.getColumns();

			for (String key : columns.keySet()) {
				OneColumnEntity info = columns.get(key);
				Field field = info.getField();

				Object value = map.get(info.getColumnName());

				// set value of any type
				field.set(model, value);
			}

			return true;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner select model list, no triggers, origin model is not changed
	 * 
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseModel> List<T> innerSelectModelList(T model, List<Long> ids, OneEntity entity)
			throws CoreException {

		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// set id list
			entity.setIds(ids);

			// call mapper
			List<Map<String, Object>> maps = oneMapper.selectByIDs(entity);

			// check existence
			if (maps == null) {
				return null;
			}

			List<T> list = new ArrayList<T>(maps.size());

			// make models
			for (Map<String, Object> map : maps) {

				// create new model
				T newmodel = (T) model.clone();

				// set id from map
				Long newid = (Long) map.get("id");
				if (newid != null) {

					newmodel.setId(newid);

					// map column to field
					Map<String, OneColumnEntity> columns = entity.getColumns();

					for (String key : columns.keySet()) {
						OneColumnEntity info = columns.get(key);
						Field field = info.getField();

						Object value = map.get(info.getColumnName());

						// set value of any type
						field.set(newmodel, value);
					}

					list.add(newmodel);
				}
			}
			return list;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner insert, no triggers, no change log
	 * 
	 * @param model
	 * @param entity
	 * @throws Exception
	 */
	protected <T extends BaseModel> void innerInsertModel(T model, OneEntity entity) throws CoreException {
		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);

				// create table if not exists
				this.CreateTable(entity);
			}

			// insert the given element
			int ret = oneMapper.insertByID(entity);

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
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseModel> T innerUpdateModel(T model, OneEntity entity) throws CoreException {
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

				boolean exists = this.innerSelectModel(old, model.getId(), entity);

				if (!exists) {
					// not exists, throw
					throw new DataNotExistsException("data for update not exists");
				}
			}

			// update the given element
			int ret = oneMapper.updateByID(entity);

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
	 * @throws Exception
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

				boolean exists = this.innerSelectModel(old, model.getId(), entity);

				if (!exists) {
					// not exists, throw
					throw new DataNotExistsException("data for delete not exists");
				}
			}

			// delete the given element
			int ret = oneMapper.deleteByID(entity);

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
	 * @see org.yaen.starter.common.data.services.ModelService#selectModel(org.yaen.starter.common.data.models.BaseModel,
	 *      long)
	 */
	@Override
	public <T extends BaseModel> void selectModel(T model, long id) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before select
		if (model.BeforeSelect(this)) {

			boolean exists = this.innerSelectModel(model, id, null);

			if (!exists) {
				// not exists
				throw new DataNotExistsException("data for select not exists");
			}

			// trigger after select
			model.AfterSelect(this);

		} else {
			// select canceled
			throw new OperationCancelledException("select cancelled by trigger");
		}

	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#trySelectModel(org.yaen.starter.common.data.models.BaseModel,
	 *      long)
	 */
	@Override
	public <T extends BaseModel> boolean trySelectModel(T model, long id) throws CoreException {
		try {
			this.selectModel(model, id);
			return true;
		} catch (DataNotExistsException ex) {
			return false;
		} catch (OperationCancelledException ex) {
			return false;
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#selectModelList(org.yaen.starter.common.data.models.BaseModel,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseModel> List<T> selectModelList(T model, List<Long> ids) throws CoreException {
		AssertUtil.notNull(model);
		AssertUtil.notNull(ids);

		// trigger before select, only once is ok
		if (model.BeforeSelect(this)) {

			List<T> list = this.innerSelectModelList(model, ids, null);

			// trigger after select each
			for (int i = 0; i < list.size(); i++) {
				list.get(i).AfterSelect(this);
			}

			return list;

		} else {
			// select canceled
			throw new OperationCancelledException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#insertModel(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> long insertModel(T model) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before insert
		if (model.BeforeInsert(this)) {

			// inner insert
			this.innerInsertModel(model, null);

			// save change log
			if (model.isEnableChangeLog()) {
				Changelog logmodel = new Changelog(SqlTypes.INSERT, null, model);
				logmodel.BeforeInsert(this);
				this.innerInsertModel(logmodel, null);
			}

			// id already set into entity and bridged to model

			// trigger after insert
			model.AfterInsert(this);
		}

		return model.getId();
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#updateModel(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> void updateModel(T model) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before update
		if (model.BeforeUpdate(this)) {

			// update model and return old one if need
			T old = this.innerUpdateModel(model, null);

			// save change log
			if (model.isEnableChangeLog()) {
				Changelog logmodel = new Changelog(SqlTypes.UPDATE, old, model);
				logmodel.BeforeInsert(this);
				this.innerInsertModel(logmodel, null);
			}

			// trigger after update
			model.AfterUpdate(this);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#deleteModel(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> void deleteModel(T model) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before delete
		if (model.BeforeDelete(this)) {

			// try get old one
			T old = this.innerDeleteModel(model, null);

			// save change log
			if (model.isEnableChangeLog()) {
				Changelog logmodel = new Changelog(SqlTypes.DELETE, old, null);
				logmodel.BeforeInsert(this);
				this.innerInsertModel(logmodel, null);
			}

			// trigger after delete
			model.AfterDelete(this);
		}
	}

}
