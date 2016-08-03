package org.yaen.starter.core.model.user.models;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.user.entities.UserEntity;
import org.yaen.starter.core.model.user.entities.UserInfoExEntity;

/**
 * user model
 * <p>
 * with extend user extend info
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class UserModel extends TwoModel {

	@Override
	public UserEntity getEntity() {
		return (UserEntity) super.getEntity();
	}

	/** get the user extend info */
	// @Getter
	private UserInfoExEntity userInfoEx;

	/**
	 * calculate password hash with given salt
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
	protected String calculatePasswordHash(String password, String salt) {
		return password + salt;
	}

	/**
	 * @param proxy
	 */
	public UserModel(ProxyService proxy) {
		super(proxy, new UserEntity());
	}

	/**
	 * get user extend info
	 * 
	 * @return
	 * @throws CommonException
	 * @throws DataException
	 */
	public UserInfoExEntity getUserInfoEx() throws DataException, CommonException {
		if (this.userInfoEx == null) {
			if (StringUtil.isNotBlank(this.getEntity().getId())) {
				UserInfoExEntity info = new UserInfoExEntity();
				info.setId(this.getEntity().getId());
				this.fillEntityById(info);

				// set to member
				this.userInfoEx = info;
			}
		}
		return userInfoEx;
	}

	/**
	 * check user credentials
	 * 
	 * @param password
	 * @param passwordHash
	 * @param passwordSalt
	 * @return
	 */
	public boolean checkUserCredentials(String password, String passwordHash, String passwordSalt) {

		// TODO here may set the last login time

		// calculate hash
		String hash = this.calculatePasswordHash(password, passwordSalt);

		// check hash, just same
		return StringUtil.equals(hash, passwordHash);
	}

}
