package org.yaen.starter.common.dal.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.dal.entities.MyDescribeEntity;
import org.yaen.starter.common.dal.entities.OneColumnEntity;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.dal.mappers.OneMapper;
import org.yaen.starter.common.dal.mappers.ZeroMapper;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsCommonException;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * one entity service for one entity
 * <p>
 * only one entity is acceptable, any other will be error
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class OneEntityServiceImpl implements EntityService {

	/** table already created */
	private static Set<String> tableSet = new HashSet<String>();

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
		if (entity == null)
			return;

		// check cache
		String entity_name = entity.getClass().getName();
		if (tableSet.contains(entity_name))
			return;

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

		// add to cache
		tableSet.add(entity_name);
	}

	/**
	 * inner select entity by rowid, fill entity fields, no triggers
	 * 
	 * @param entity
	 * @param rowid
	 * @return
	 * @throws CoreException
	 */
	protected <T extends OneEntity> boolean innerSelectEntityByRowid(T entity, long rowid) throws CommonException {
		try {
			// create table if not exists
			this.CreateTable(entity);

			// set rowid
			entity.setRowid(rowid);

			// call mapper
			Map<String, Object> values = oneMapper.selectByRowid(entity);

			// check existence
			if (values == null) {
				return false;
			}

			// fill values
			entity.fillValues(values);

			return true;
		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * inner insert
	 * 
	 * @param entity
	 * @throws CoreException
	 */
	protected <T extends OneEntity> void innerInsertEntityByRowid(T entity) throws CommonException {
		try {
			// create table if not exists
			this.CreateTable(entity);

			// insert the given element
			int ret = oneMapper.insertByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new CommonException("insert failed");
			}

			// id already set into entity and bridged to entity

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * inner update
	 * 
	 * @param entity
	 * @return
	 * @throws CommonException
	 */
	protected <T extends OneEntity> void innerUpdateEntityByRowid(T entity) throws CommonException {
		try {
			// create table if not exists
			this.CreateTable(entity);

			// update the given element
			int ret = oneMapper.updateByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new CommonException("update failed");
			}

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * inner delete
	 * 
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	protected <T extends OneEntity> void innerDeleteEntity(T entity) throws CommonException {
		try {
			// create table if not exists
			this.CreateTable(entity);

			// delete the given element
			int ret = oneMapper.deleteByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new CommonException("delete failed");
			}

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.EntityService#selectEntityByRowid(org.yaen.starter.common.data.entities.BaseEntity,
	 *      long)
	 */
	@Override
	public <T extends BaseEntity> void selectEntityByRowid(T entity, long rowid) throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		boolean exists = this.innerSelectEntityByRowid((OneEntity) entity, rowid);

		if (!exists) {
			// not exists
			throw new DataNotExistsCommonException("data not exists");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.EntityService#insertEntityByRowid(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> long insertEntityByRowid(T entity) throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		// inner insert
		this.innerInsertEntityByRowid((OneEntity) entity);

		// id already set into entity and bridged to entity

		return entity.getRowid();
	}

	/**
	 * @see org.yaen.starter.common.data.services.EntityService#updateEntityByRowid(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void updateEntityByRowid(T entity) throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		// update
		this.innerUpdateEntityByRowid((OneEntity) entity);
	}

	/**
	 * @see org.yaen.starter.common.data.services.EntityService#deleteEntityByRowid(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void deleteEntityByRowid(T entity) throws CommonException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		// delete
		this.innerDeleteEntity((OneEntity) entity);

	}

	/**
	 * @see org.yaen.starter.common.data.services.EntityService#trySelectEntityByRowid(org.yaen.starter.common.data.entities.BaseEntity,
	 *      long)
	 */
	@Override
	public <T extends BaseEntity> boolean trySelectEntityByRowid(T entity, long rowid) throws CommonException {
		try {
			this.selectEntityByRowid(entity, rowid);
			return true;
		} catch (DataNotExistsCommonException ex) {
			return false;
		}
	}

}
