package org.yaen.starter.common.dal.services.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.dal.mappers.EntityMapper;
import org.yaen.starter.common.dal.services.TableService;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.exceptions.NoDataAffectedException;
import org.yaen.starter.common.data.exceptions.OperationCancelledCommonException;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.util.utils.AssertUtil;

/**
 * one entity service for one entity
 * <p>
 * only one entity is acceptable, any other will be error
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class OneEntityServiceImpl implements EntityService {

	@Autowired
	private EntityMapper oneMapper;

	@Autowired
	private TableService tableService;

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
			tableService.CreateTable(entity);

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
	 * @throws NoDataAffectedException
	 * @throws DuplicateDataException
	 * @throws CoreException
	 */
	protected <T extends OneEntity> void innerInsertEntityByRowid(T entity)
			throws CommonException, NoDataAffectedException, DuplicateDataException {
		try {
			// create table if not exists
			tableService.CreateTable(entity);

			// insert the given element
			int ret = oneMapper.insertByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new NoDataAffectedException("insert failed");
			}

			// id already set into entity and bridged to entity

		} catch (NoDataAffectedException ex) {
			throw ex;
		} catch (DuplicateKeyException ex) {
			throw new DuplicateDataException("duplicate data", ex);
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
	 * @throws NoDataAffectedException
	 */
	protected <T extends OneEntity> void innerUpdateEntityByRowid(T entity)
			throws CommonException, NoDataAffectedException {
		try {
			// create table if not exists
			tableService.CreateTable(entity);

			// update the given element
			int ret = oneMapper.updateByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new NoDataAffectedException("update failed");
			}

		} catch (NoDataAffectedException ex) {
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
	 * @throws NoDataAffectedException
	 * @throws CoreException
	 */
	protected <T extends OneEntity> void innerDeleteEntity(T entity) throws CommonException, NoDataAffectedException {
		try {
			// create table if not exists
			tableService.CreateTable(entity);

			// delete the given element
			int ret = oneMapper.deleteByRowid(entity);

			if (ret <= 0) {
				// execute fail
				throw new NoDataAffectedException("delete failed");
			}

		} catch (NoDataAffectedException ex) {
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
	public <T extends BaseEntity> void selectEntityByRowid(T entity, long rowid)
			throws CommonException, DataNotExistsException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		OneEntity one = (OneEntity) entity;

		// trigger
		if (one.BeforeSelect()) {

			boolean exists = this.innerSelectEntityByRowid(one, rowid);

			if (!exists) {
				// not exists
				throw new DataNotExistsException("data not exists");
			}

			// trigger
			one.AfterSelect();

		} else {
			// select canceled
			throw new OperationCancelledCommonException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.EntityService#insertEntityByRowid(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> long insertEntityByRowid(T entity)
			throws CommonException, NoDataAffectedException, DuplicateDataException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		OneEntity one = (OneEntity) entity;

		// trigger
		if (one.BeforeInsert()) {

			// inner insert
			this.innerInsertEntityByRowid(one);

			// id already set into entity and bridged to entity

			// trigger
			one.AfterInsert();

		} else {
			// canceled
			throw new OperationCancelledCommonException("insert cancelled by trigger");
		}

		return entity.getRowid();
	}

	/**
	 * @see org.yaen.starter.common.data.services.EntityService#updateEntityByRowid(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void updateEntityByRowid(T entity) throws CommonException, NoDataAffectedException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		OneEntity one = (OneEntity) entity;

		// trigger
		if (one.BeforeUpdate()) {

			// update
			this.innerUpdateEntityByRowid(one);

			// trigger
			one.AfterUpdate();

		} else {
			// canceled
			throw new OperationCancelledCommonException("update cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.EntityService#deleteEntityByRowid(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void deleteEntityByRowid(T entity) throws CommonException, NoDataAffectedException {
		AssertUtil.notNull(entity);
		AssertUtil.isInstanceOf(OneEntity.class, entity, "only support OneEntity");

		OneEntity one = (OneEntity) entity;

		// trigger
		if (one.BeforeDelete()) {

			// delete
			this.innerDeleteEntity((OneEntity) entity);

			// trigger
			one.AfterDelete();

		} else {
			// canceled
			throw new OperationCancelledCommonException("delete cancelled by trigger");
		}
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
		} catch (DataNotExistsException ex) {
			return false;
		} catch (OperationCancelledCommonException ex) {
			return false;
		}
	}

}
