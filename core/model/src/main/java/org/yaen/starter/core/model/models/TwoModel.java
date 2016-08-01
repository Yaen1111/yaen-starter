package org.yaen.starter.core.model.models;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
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
	 * @return
	 * @throws DataException
	 * @throws CommonException
	 */
	protected <T extends OneEntity> void fillEntityById(T entity) throws DataException, CommonException {

		// get row ids
		List<Long> rowids = this.proxy.getQueryService().selectRowidsByField(entity, "id");

		// check empty
		if (rowids == null || rowids.isEmpty()) {
			throw new DataNotExistsException("data not exists, id=" + entity.getId());
		}

		// check duplicate
		if (rowids.size() > 1) {
			throw new DuplicateDataException("data id duplicate, id=" + entity.getId());
		}

		// fill
		this.proxy.getEntityService().selectEntityByRowid(entity, rowids.get(0));
	}

	/**
	 * fill entity by unique fields, the value is set in the entity
	 * 
	 * @param entity
	 * @param fields
	 * @throws DataException
	 * @throws CommonException
	 */
	protected <T extends OneEntity> void fillEntityByUniqueFields(T entity, String[] fields)
			throws DataException, CommonException {

		// get row ids
		List<Long> rowids = this.proxy.getQueryService().selectRowidsByFields(entity, fields);

		// check empty
		if (rowids == null || rowids.isEmpty()) {
			throw new DataNotExistsException("data not exists with given fields");
		}

		// check duplicate
		if (rowids.size() > 1) {
			throw new DuplicateDataException("duplicate data found with given fields");
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
	 * read cache by key
	 * 
	 * @param key
	 * @return
	 */
	protected String readCache(String key) {
		return (String) this.proxy.getCacheService().get(key);
	}

	/**
	 * write cache by key
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 */
	protected void writeCache(String key, String value, int expire) {
		this.proxy.getCacheService().set(key, value, expire);
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
	 * load model entity by id
	 * 
	 * @param id
	 * @throws CommonException
	 * @throws DataException
	 */
	public void loadById(String id) throws DataException, CommonException {
		AssertUtil.notBlank(id);

		// set id to entity
		this.entity.setId(id);
		this.fillEntityById(this.entity);
	}

	/**
	 * load by id, if not exists, create new one
	 * 
	 * @param id
	 * @throws DataException
	 * @throws CommonException
	 */
	public void loadOrCreateById(String id) throws DataException, CommonException {
		AssertUtil.notBlank(id);

		try {
			// set id to entity
			this.entity.setId(id);
			this.fillEntityById(this.entity);
		} catch (DataNotExistsException ex) {
			// set id to entity again
			this.entity.setId(id);
			this.insertEntity(this.entity);
		}
	}

	/**
	 * save new entity
	 * 
	 * @throws CommonException
	 * @throws DataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveNew() throws DataException, CommonException {
		this.insertEntity(this.entity);
	}

	/**
	 * update model by id
	 * 
	 * @throws CommonException
	 * @throws DataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveById() throws DataException, CommonException {
		this.updateEntity(this.entity);
	}

	/**
	 * delete model by id
	 * 
	 * @throws CommonException
	 * @throws DataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteById() throws DataException, CommonException {
		this.deleteEntity(this.entity);
	}

}
