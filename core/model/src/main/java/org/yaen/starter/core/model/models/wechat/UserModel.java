package org.yaen.starter.core.model.models.wechat;

import org.yaen.starter.common.dal.entities.wechat.UserEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.exceptions.NoDataAffectedException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;

/**
 * the wechat user model, mostly for openid + appid
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
public class UserModel extends TwoModel<UserEntity> {

	/**
	 * @param proxy
	 * @param sample
	 */
	public UserModel(ProxyService proxy, UserEntity sample) {
		super(proxy, sample);
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
	public void loadByOpenId(String openId, String appId)
			throws CoreException, DataNotExistsException, DuplicateDataException {
		AssertUtil.notBlank(openId);
		AssertUtil.notBlank(appId);

		// clear first
		this.clear();

		// get user by openid + appid
		try {
			this.entity = this.proxy.getQueryService().selectOneByUniqueFields(new UserEntity(openId, appId),
					new String[] { "OPEN_ID", "APP_ID" });
		} catch (CommonException ex) {
			throw new CoreException("load user failed", ex);
		}
	}

	/**
	 * create new by openid+appid
	 * 
	 * @param openId
	 * @param appId
	 * @throws CoreException
	 * @throws DuplicateDataException
	 */
	public void createNewByOpenId(String openId, String appId) throws CoreException, DuplicateDataException {
		AssertUtil.notBlank(openId);
		AssertUtil.notBlank(appId);

		try {
			// create new entity
			UserEntity user = new UserEntity(openId, appId);

			// do insert
			this.proxy.getEntityService().insertEntityByRowid(user);

		} catch (NoDataAffectedException ex) {
			throw new CoreException("create model failed", ex);
		} catch (CommonException ex) {
			throw new CoreException("create model failed", ex);
		}

	}
}
