package org.yaen.starter.core.model.models.wechat;

import org.yaen.starter.common.dal.entities.wechat.UserEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.services.ProxyService;

import lombok.Getter;

/**
 * the wechat user model, mostly for openid + appid
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
public class UserModel extends OneModel {

	@Getter
	private ProxyService service;

	/** the main user entity */
	private UserEntity user;

	/**
	 * construct new model with service
	 * 
	 * @param service
	 */
	public UserModel(ProxyService service) {
		super("1.0.0");

		this.service = service;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#check()
	 */
	@Override
	public void check() throws CoreException {
		if (this.user == null)
			throw new CoreException("model not loaded");
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#clear()
	 */
	@Override
	public void clear() {
		this.user = null;
	}

	/**
	 * load user by openid + appid
	 * 
	 * @param openId
	 * @param appId
	 * @throws CoreException
	 * @throws DuplicateDataException
	 * @throws DataNotExistsException
	 */
	public void load(String openId, String appId) throws CoreException, DataNotExistsException, DuplicateDataException {
		AssertUtil.notBlank(openId);
		AssertUtil.notBlank(appId);

		// clear first
		this.clear();

		// get user by openid + appid
		try {
			this.user = this.service.getQueryService().selectOneByUniqueFields(new UserEntity(openId, appId),
					new String[] { "OPEN_ID", "APP_ID" });
		} catch (CommonException ex) {
			throw new CoreException("load user failed", ex);
		}
	}

	/**
	 * insert or update data
	 * @throws CoreException 
	 */
	public void save() throws CoreException {
		this.check();
		
		

	}

}
