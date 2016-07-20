package org.yaen.starter.core.model.models;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.services.ProxyService;

import lombok.Getter;

/**
 * default model implement with proxy service and default entity
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public class TwoModel extends OneModel {

	/** the none-typed entity, child can override with sub-typed entity */
	@Getter
	private OneEntity entity;

	/** the proxy */
	@Getter
	protected ProxyService proxy;

	/**
	 * fill by id
	 * 
	 * @param entity
	 * @param id
	 * @return
	 * @throws DataException
	 * @throws CommonException
	 */
	protected <T extends OneEntity> void fillEntityById(T entity, String id) throws DataException, CommonException {

		// set id to entity
		entity.setId(id);
		List<Long> rowids = this.proxy.getQueryService().selectRowidsByField(entity, "id");

		// check empty
		if (rowids == null || rowids.isEmpty()) {
			throw new DataNotExistsException("data not exists, id=" + id);
		}

		// check duplicate
		if (rowids.size() > 1) {
			throw new DuplicateDataException("data id duplicate, id=" + id);
		}

		// fill
		this.proxy.getEntityService().selectEntityByRowid(entity, rowids.get(0));
	}

	/**
	 * select by id
	 * 
	 * @param entity
	 * @param id
	 * @return
	 * @throws DataException
	 * @throws CommonException
	 */
	protected <T extends OneEntity> T selectEntityById(T entity, String id) throws DataException, CommonException {
		return this.proxy.getQueryService().selectOneById(entity, id);
	}

	/**
	 * insert entity
	 * 
	 * @param entity
	 * @throws DataException
	 * @throws CommonException
	 */
	protected void insertEntity(OneEntity entity) throws DataException, CommonException {
		this.proxy.getEntityService().insertEntityByRowid(entity);
	}

	/**
	 * update entity
	 * 
	 * @param entity
	 * @throws DataException
	 * @throws CommonException
	 */
	protected void updateEntity(OneEntity entity) throws DataException, CommonException {
		this.proxy.getEntityService().updateEntityByRowid(entity);
	}

	/**
	 * delete entity
	 * 
	 * @param entity
	 * @throws DataException
	 * @throws CommonException
	 */
	protected void deleteEntity(OneEntity entity) throws DataException, CommonException {
		this.proxy.getEntityService().deleteEntityByRowid(entity);
	}

	/**
	 * constructor with proxy service and entity
	 * 
	 * @param proxy
	 * @param entity
	 */
	public TwoModel(ProxyService proxy, OneEntity entity) {
		super();

		this.proxy = proxy;

		// use override setter
		this.entity = entity;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#check()
	 */
	@Override
	public void check() throws CoreException {
	}

	/**
	 * load model entity by id
	 * 
	 * @param id
	 * @throws CommonException
	 * @throws DataException
	 */
	public void loadById(String id) throws DataException, CommonException {
		AssertUtil.notBlank(id);

		this.fillEntityById(this.entity, id);
	}

	/**
	 * save new entity
	 * 
	 * @param id
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveNew() throws CoreException, DataException, CommonException {
		this.check();

		this.insertEntity(this.entity);
	}

	/**
	 * update model by id
	 * 
	 * @throws CommonException
	 * @throws DataException
	 * @throws CoreException
	 * 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveById() throws DataException, CommonException, CoreException {
		this.check();

		this.updateEntity(this.entity);
	}

	/**
	 * delete model by id
	 * 
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteById() throws CoreException, DataException, CommonException {
		this.check();

		this.deleteEntity(this.entity);
	}

}
