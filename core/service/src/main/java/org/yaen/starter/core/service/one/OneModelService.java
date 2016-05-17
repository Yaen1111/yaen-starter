/**
 * 
 */
package org.yaen.starter.core.service.one;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.yaen.starter.common.dal.mappers.OneMapper;
import org.yaen.starter.common.dal.mappers.ZeroMapper;
import org.yaen.starter.common.data.entities.AnotherEntity;
import org.yaen.starter.common.data.entities.MyDescribeEntity;
import org.yaen.starter.common.data.entities.OneColumnEntity;
import org.yaen.starter.common.data.entities.OneEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.one.OneModel;

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
			describes = zeroMapper.describeTable(entity);
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
	 * inner select model
	 * 
	 * @param model
	 * @param id
	 * @throws Exception
	 */
	protected <T extends BaseModel> boolean innerSelectModel(T model, OneEntity entity) throws Exception {

		// call mapper
		Map<String, Object> map = oneMapper.selectByID(entity);

		// check existence
		if (map == null) {
			return false;
		}

		// set id
		model.setId(entity.getId());

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
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.ModelService#selectModel(org.yaen.starter.common.data.models.BaseModel,
	 *      long)
	 */
	@Override
	public <T extends BaseModel> void selectModel(T model, long id) throws Exception {
		Assert.notNull(model);

		// get another entity
		AnotherEntity entity = new AnotherEntity(model);

		this.CreateTable(entity);

		// set given id
		entity.setId(id);

		boolean exists = this.innerSelectModel(model, entity);

		if (!exists) {
			// not exists
			throw new CoreException("data not exist");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#trySelectModel(org.yaen.starter.common.data.models.BaseModel,
	 *      long)
	 */
	@Override
	public <T extends BaseModel> boolean trySelectModel(T model, long id) throws Exception {
		try {
			this.selectModel(model, id);
			return true;
		} catch (CoreException ce) {
			return false;
		}
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.ModelService#selectModelList(org.yaen.starter.common.data.models.BaseModel,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseModel> List<T> selectModelList(T model, List<Long> ids) throws Exception {
		Assert.notNull(model);

		List<T> list = new ArrayList<T>();

		if (ids != null) {
			for (Long id : ids) {
				@SuppressWarnings("unchecked")
				T attr = (T) model.clone();

				this.selectModel(attr, id);
				list.add(attr);
			}
		}

		return list;
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.ModelService#insertModel(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> long insertModel(T model) throws Exception {
		Assert.notNull(model);

		// trigger before insert
		model.BeforeInsert(this);

		// get another entity
		AnotherEntity entity = new AnotherEntity(model);

		// create table if not exists
		this.CreateTable(entity);

		// insert the given element
		int ret = oneMapper.insertByID(entity);

		if (ret <= 0) {
			// execute fail
			throw new CoreException("insert failed");
		}

		// id already set into entity and bridged to model

		// trigger after insert
		model.AfterInsert(this);

		return model.getId();
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.ModelService#updateModel(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> void updateModel(T model) throws Exception {
		Assert.notNull(model);

		// trigger before update
		model.BeforeUpdate(this);

		// get another entity
		AnotherEntity entity = new AnotherEntity(model);

		// create table if not exists
		this.CreateTable(entity);

		// try get old one
		BaseModel old;

		{
			old = (BaseModel) model.clone();

			Boolean exists = this.innerSelectModel(old, entity);

			if (!exists) {
				// already exists, throw
				throw new CoreException("data not exists");
			}
		}

		// update the given element
		int ret = oneMapper.updateByID(entity);

		if (ret <= 0) {
			// execute fail
			throw new CoreException("update failed");
		}

		// trigger after update
		model.AfterUpdate(this);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.ModelService#deleteModel(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> void deleteModel(T model) throws Exception {
		Assert.notNull(model);

		// trigger before delete
		model.BeforeDelete(this);

		// get another entity
		AnotherEntity entity = new AnotherEntity(model);

		// create table if not exists
		this.CreateTable(entity);

		// try get old one
		OneModel old;

		{
			old = (OneModel) model.clone();

			Boolean exists = this.innerSelectModel(old, entity);

			if (!exists) {
				// not exists, throw
				throw new CoreException("data not exists");
			}
		}

		// delete the given element
		int ret = oneMapper.deleteByID(entity);

		if (ret <= 0) {
			// execute fail
			throw new CoreException("delete failed");
		}

		// trigger after delete
		model.AfterDelete(this);
	}

}
