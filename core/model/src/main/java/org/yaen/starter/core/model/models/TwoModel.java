package org.yaen.starter.core.model.models;

import org.springframework.transaction.annotation.Transactional;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.exceptions.NoDataAffectedException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.services.ProxyService;

import lombok.Getter;

/**
 * default model implement with proxy service
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public class TwoModel<T extends OneEntity> extends OneModel {

	/** the proxy */
	@Getter
	protected ProxyService proxy;

	/** the sample entity */
	protected T sample;

	/** the main entity */
	@Getter
	protected T entity;

	/**
	 * constructor with proxy service, with proxy and sample entity
	 * 
	 * @param proxy
	 * @param sample
	 */
	public TwoModel(ProxyService proxy, T sample) {
		super();

		this.proxy = proxy;
		this.sample = sample;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#check()
	 */
	@Override
	public void check() throws CoreException {
		if (this.entity == null)
			throw new CoreException("main entity not loaded");
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#clear()
	 */
	@Override
	public void clear() {
		this.entity = null;
	}

	/**
	 * load model entity by id
	 * 
	 * @param id
	 * @throws DataNotExistsException
	 * @throws CoreException
	 */
	public void loadById(String id) throws DataNotExistsException, CoreException {
		AssertUtil.notBlank(id);

		// clear first
		this.clear();

		// get entity
		try {
			this.entity = this.proxy.getQueryService().selectOneById(this.sample, id);
		} catch (CommonException ex) {
			throw new CoreException("load user failed", ex);
		} catch (DuplicateDataException ex) {
			throw new CoreException("load user failed", ex);
		}
	}

	/**
	 * create new model with given id only, model is not affected
	 * 
	 * @param id
	 * @throws CoreException
	 * @throws DuplicateDataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createNewById(String id) throws CoreException, DuplicateDataException {
		AssertUtil.notBlank(id);

		try {
			// create new entity
			OneEntity newentity = this.sample.getClass().newInstance();

			newentity.setId(id);

			// do insert
			this.proxy.getEntityService().insertEntityByRowid(newentity);

		} catch (InstantiationException | IllegalAccessException ex) {
			throw new CoreException("create model failed", ex);
		} catch (NoDataAffectedException ex) {
			throw new CoreException("create model failed", ex);
		} catch (CommonException ex) {
			throw new CoreException("create model failed", ex);
		}

	}

	/**
	 * update model by id
	 * 
	 * @throws CoreException
	 * @throws DataNotExistsException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveById() throws CoreException, DataNotExistsException {
		this.check();

		try {
			this.proxy.getEntityService().updateEntityByRowid(this.entity);
		} catch (NoDataAffectedException ex) {
			// no data, means data not exists
			throw new DataNotExistsException("save model failed", ex);
		} catch (CommonException ex) {
			throw new CoreException("save model failed", ex);
		}

	}

	/**
	 * delete model by id
	 * 
	 * @throws CoreException
	 * @throws DataNotExistsException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteById() throws CoreException, DataNotExistsException {
		this.check();

		try {
			this.proxy.getEntityService().deleteEntityByRowid(this.entity);
		} catch (NoDataAffectedException ex) {
			// no data, means data not exists
			throw new DataNotExistsException("delete model failed", ex);
		} catch (CommonException ex) {
			throw new CoreException("delete model failed", ex);
		}

	}

}