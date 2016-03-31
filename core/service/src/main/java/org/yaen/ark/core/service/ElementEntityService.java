/**
 * 
 */
package org.yaen.ark.core.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.yaen.ark.core.model.entities.BaseElement;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.ark.core.model.utils.ElementUtil;
import org.yaen.spring.data.entities.BaseEntity;
import org.yaen.spring.common.exceptions.BizException;
import org.yaen.spring.data.mappers.ElementMapper;
import org.yaen.spring.data.mappers.TableMapper;
import org.yaen.spring.data.models.DescribeModel;
import org.yaen.spring.data.models.ElementInfo;
import org.yaen.spring.data.models.ElementModel;
import org.yaen.spring.data.services.EntityService;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import org.yaen.spring.common.utils.StringUtil;

/**
 * element service for most operation
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@Service
public class ElementEntityService implements EntityService {

	@Autowired
	private ElementMapper elementMapper;

	@Autowired
	private TableMapper tableMapper;

	/**
	 * create table if not exists, or alter table if columns differs
	 * 
	 * @throws Exception
	 */
	protected void CreateTable(ElementModel model) throws Exception {

		List<DescribeModel> describes = null;

		// describe table, if table not exists, throw exception
		try {
			describes = tableMapper.describeTable(model);
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
			tableMapper.createTable(model);

		} else {

			// exists, try to check column type and missed column
			// loop every element, and add/mod if not suitable
			for (Entry<String, ElementInfo> entry : model.getColumns().entrySet()) {
				boolean exists = false;

				for (DescribeModel describe : describes) {
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
								model.setModifyFieldName(entry.getKey());
								tableMapper.modifyColumn(model);
							}
						}

						break;
					}
				} // for

				if (!exists) {
					// no column, try to add one
					model.setAddFieldName(entry.getKey());
					tableMapper.addColumn(model);
				}
			} // for
		} // describes == null

	}

	/**
	 * inner select element
	 * 
	 * 
	 * @param element
	 * @param id
	 * @throws Exception
	 */
	protected <T extends BaseEntity> Boolean innerSelectElement(T element, ElementModel model) throws Exception {

		// call mapper
		Map<String, Object> map = elementMapper.selectByID(model);

		// check existence
		if (map == null) {
			return false;
		}

		// set id
		element.setId(model.getId());

		// map column to field
		Map<String, ElementInfo> columns = model.getColumns();

		for (String key : columns.keySet()) {
			ElementInfo info = columns.get(key);
			Field field = info.getField();

			Object value = map.get(info.getColumnName());

			// set value of any type
			field.set(element, value);
		}

		return true;
	}

	/**
	 * 
	 * @see org.yaen.arkcore.core.model.services.EntityService#selectEntity(org.yaen.arkcore.core.model.entities.BaseEntity,
	 *      long)
	 */
	@Override
	public <T extends BaseEntity> void selectEntity(T entity, long id) throws Exception {
		Assert.notNull(entity);

		// get element model
		ElementModel model = ElementUtil.GetElementModel(entity);

		this.CreateTable(model);

		// set given id
		model.setId(id);

		Boolean exists = this.innerSelectElement(entity, model);

		if (!exists) {
			// not exists
			throw new BizException("data not exist");
		}

	}

	/**
	 * 
	 * @see org.yaen.arkcore.core.model.services.EntityService#selectEntityList(org.yaen.arkcore.core.model.entities.BaseEntity,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseEntity> List<T> selectEntityList(T entity, List<Long> ids) throws Exception {
		Assert.notNull(entity);

		List<T> list = new ArrayList<T>();

		if (ids != null) {
			for (Long id : ids) {
				@SuppressWarnings("unchecked")
				T attr = (T) entity.clone();

				this.selectEntity(attr, id);
				list.add(attr);
			}
		}

		return list;
	}

	/**
	 * 
	 * @see org.yaen.arkcore.core.model.services.EntityService#insertEntity(org.yaen.arkcore.core.model.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> long insertEntity(T entity) throws Exception {
		Assert.notNull(entity);

		// trigger before insert
		entity.BeforeInsert(this);

		// get element model
		ElementModel model = ElementUtil.GetElementModel(entity);

		// create table if not exists
		this.CreateTable(model);

		// try get old one
		BaseElement old;

		{
			old = (BaseElement) entity.clone();

			Boolean exists = this.innerSelectElement(old, model);

			if (exists) {
				// already exists, throw
				throw new BizException("data already exists");
			}
		}

		// insert the given element
		int ret = elementMapper.insertByID(model);

		if (ret <= 0) {
			// execute fail
			throw new BizException("insert failed");
		}

		// set back id from model
		entity.setId(model.getId());

		// trigger after insert
		entity.AfterInsert(this);

		return entity.getId();
	}

	/**
	 * 
	 * @see org.yaen.arkcore.core.model.services.EntityService#updateElement(org.yaen.arkcore.core.model.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void updateElement(T entity) throws Exception {
		Assert.notNull(entity);

		// trigger before update
		entity.BeforeUpdate(this);

		// get element model
		ElementModel model = ElementUtil.GetElementModel(entity);

		// create table if not exists
		this.CreateTable(model);

		// try get old one
		BaseEntity old;

		{
			old = (BaseEntity) entity.clone();

			Boolean exists = this.innerSelectElement(old, model);

			if (!exists) {
				// already exists, throw
				throw new BizException("data not exists");
			}
		}

		// update the given element
		int ret = elementMapper.updateByID(model);

		if (ret <= 0) {
			// execute fail
			throw new BizException("update failed");
		}

		// trigger after update
		entity.AfterUpdate(this);
	}

	/**
	 * 
	 * @see org.yaen.arkcore.core.model.services.EntityService#deleteEntity(org.yaen.arkcore.core.model.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void deleteEntity(T entity) throws Exception {
		Assert.notNull(entity);

		// trigger before delete
		entity.BeforeDelete(this);

		// get element model
		ElementModel model = ElementUtil.GetElementModel(entity);

		// create table if not exists
		this.CreateTable(model);

		// try get old one
		BaseElement old;

		{
			old = (BaseElement) entity.clone();

			Boolean exists = this.innerSelectElement(old, model);

			if (!exists) {
				// not exists, throw
				throw new BizException("data not exists");
			}
		}

		// delete the given element
		int ret = elementMapper.deleteByID(model);

		if (ret <= 0) {
			// execute fail
			throw new BizException("delete failed");
		}

		// trigger after delete
		entity.AfterDelete(this);
	}

}
