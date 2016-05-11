/**
 * 
 */
package org.yaen.starter.core.service.elements;

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
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.pos.AnotherPO;
import org.yaen.starter.common.data.pos.MyDescribePO;
import org.yaen.starter.common.data.pos.OneColumnPO;
import org.yaen.starter.common.data.pos.OnePO;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.util.StringUtil;
import org.yaen.starter.core.model.elements.BaseElement;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * element entity service for most operation
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class ElementEntityService implements EntityService {

	@Autowired
	private OneMapper oneMapper;

	@Autowired
	private ZeroMapper zeroMapper;

	/**
	 * create table if not exists, or alter table if columns differs
	 * 
	 * @throws Exception
	 */
	protected void CreateTable(OnePO po) throws Exception {

		List<MyDescribePO> describes = null;

		// describe table, if table not exists, throw exception
		try {
			describes = zeroMapper.describeTable(po);
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
			zeroMapper.createTable(po);

		} else {

			// exists, try to check column type and missed column
			// loop every element, and add/mod if not suitable
			for (Entry<String, OneColumnPO> entry : po.getColumns().entrySet()) {
				boolean exists = false;

				for (MyDescribePO describe : describes) {
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
								// model.setModifyFieldName(entry.getKey());
								zeroMapper.modifyColumn(po);
							}
						}

						break;
					}
				} // for

				if (!exists) {
					// no column, try to add one
					// model.setAddFieldName(entry.getKey());
					zeroMapper.addColumn(po);
				}
			} // for
		} // describes == null

	}

	/**
	 * inner select entity
	 * 
	 * @param entity
	 * @param id
	 * @throws Exception
	 */
	protected <T extends BaseEntity> boolean innerSelectEntity(T entity, OnePO po) throws Exception {

		// call mapper
		Map<String, Object> map = oneMapper.selectByID(po);

		// check existence
		if (map == null) {
			return false;
		}

		// set id
		entity.setId(po.getId());

		// map column to field
		Map<String, OneColumnPO> columns = po.getColumns();

		for (String key : columns.keySet()) {
			OneColumnPO info = columns.get(key);
			Field field = info.getField();

			Object value = map.get(info.getColumnName());

			// set value of any type
			field.set(entity, value);
		}

		return true;
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.EntityService#selectEntity(org.yaen.starter.common.data.entities.BaseEntity,
	 *      long)
	 */
	@Override
	public <T extends BaseEntity> void selectEntity(T entity, long id) throws Exception {
		Assert.notNull(entity);

		// get another po
		AnotherPO po = new AnotherPO(entity);

		this.CreateTable(po);

		// set given id
		po.setId(id);

		boolean exists = this.innerSelectEntity(entity, po);

		if (!exists) {
			// not exists
			throw new BizException("data not exist");
		}
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.EntityService#selectEntityList(org.yaen.starter.common.data.entities.BaseEntity,
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
	 * @see org.yaen.starter.common.data.services.EntityService#insertEntity(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> long insertEntity(T entity) throws Exception {
		Assert.notNull(entity);

		// trigger before insert
		entity.BeforeInsert(this);

		// get another po
		AnotherPO po = new AnotherPO(entity);

		// create table if not exists
		this.CreateTable(po);

		// insert the given element
		int ret = oneMapper.insertByID(po);

		if (ret <= 0) {
			// execute fail
			throw new BizException("insert failed");
		}

		// id already set into po and bridged to entity

		// trigger after insert
		entity.AfterInsert(this);

		return entity.getId();
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.EntityService#updateEntity(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void updateEntity(T entity) throws Exception {
		Assert.notNull(entity);

		// trigger before update
		entity.BeforeUpdate(this);

		// get another po
		AnotherPO po = new AnotherPO(entity);

		// create table if not exists
		this.CreateTable(po);

		// try get old one
		BaseEntity old;

		{
			old = (BaseEntity) entity.clone();

			Boolean exists = this.innerSelectEntity(old, po);

			if (!exists) {
				// already exists, throw
				throw new BizException("data not exists");
			}
		}

		// update the given element
		int ret = oneMapper.updateByID(po);

		if (ret <= 0) {
			// execute fail
			throw new BizException("update failed");
		}

		// trigger after update
		entity.AfterUpdate(this);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.EntityService#deleteEntity(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void deleteEntity(T entity) throws Exception {
		Assert.notNull(entity);

		// trigger before delete
		entity.BeforeDelete(this);

		// get another po
		AnotherPO po = new AnotherPO(entity);

		// create table if not exists
		this.CreateTable(po);

		// try get old one
		BaseElement old;

		{
			old = (BaseElement) entity.clone();

			Boolean exists = this.innerSelectEntity(old, po);

			if (!exists) {
				// not exists, throw
				throw new BizException("data not exists");
			}
		}

		// delete the given element
		int ret = oneMapper.deleteByID(po);

		if (ret <= 0) {
			// execute fail
			throw new BizException("delete failed");
		}

		// trigger after delete
		entity.AfterDelete(this);
	}

}
