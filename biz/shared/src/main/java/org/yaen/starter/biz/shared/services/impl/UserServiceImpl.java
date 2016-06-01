package org.yaen.starter.biz.shared.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.biz.shared.services.UserService;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsBizException;
import org.yaen.starter.common.data.exceptions.DuplicateDataBizException;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.user.User;

/**
 * user based actions
 * 
 * @author Yaen 2016年5月17日下午4:08:26
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelService modelService;

	@Autowired
	private QueryService queryService;

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#registerNewUser(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public void registerNewUser(UserDTO user) throws BizException {
		AssertUtil.notNull(user);

		// create new user
		User model = new User();

		// set basic info
		model.setId(user.getUserName());
		model.setPasswordHash(user.getPassword());

		// gen salt, set hash
		model.setPasswordSalt(user.getPasswordSalt());
		model.setPasswordHash(user.getPasswordHash());

		try {
			modelService.insertModelByRowid(model);
		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#getUserByName(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public void getUserByName(UserDTO user) throws BizException {
		AssertUtil.notNull(user);

		try {
			// find user
			User model = new User();

			// try to get model, used to create table
			modelService.trySelectModelByRowid(model, 0L);

			model.setId(user.getUserName());

			List<Long> ids = queryService.SelectIDsByFieldName(model, "userName");

			if (ids == null || ids.isEmpty()) {
				throw new DataNotExistsBizException("user not exists");
			}

			if (ids.size() > 1) {
				throw new DuplicateDataBizException("duplicate usernames");
			}

			// get user detail
			modelService.selectModelByRowid(model, ids.get(0));

			// do some additional check, like locked

			// user ok, set info
			user.setUserName(model.getId());
			user.setPasswordHash(model.getPasswordHash());
			user.setPasswordSalt(model.getPasswordSalt());

			// TODO how to set last login time?

		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#Logout(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public void Logout(UserDTO user) throws BizException {
		// TODO how to set last logout time?
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#checkUserCredentials(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public boolean checkUserCredentials(UserDTO user) {

		// TODO here may set the last login time

		// check password, just same
		if (StringUtil.equals(user.getPasswordHash(), user.getPassword())) {
			return true;
		}
		return false;
	}

}
