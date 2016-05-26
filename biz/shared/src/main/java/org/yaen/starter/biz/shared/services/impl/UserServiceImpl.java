package org.yaen.starter.biz.shared.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.biz.shared.services.UserService;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
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
	 * @see org.yaen.starter.biz.shared.services.UserService#RegisterNewUser(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public long RegisterNewUser(UserDTO dto) throws BizException {
		AssertUtil.notNull(dto);

		// create new user
		User user = new User();

		// set partyid if given
		if (dto.getUserID() > 0) {
			user.setId(dto.getUserID());
		}
		user.setUserName(dto.getUserName());
		user.setPasswordSalt(dto.getPasswordSalt());
		user.setPasswordHash(dto.getPasswordHash());
		long userid = 0;
		try {
			userid = modelService.insertModel(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userid;
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#Login(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public void Login(UserDTO dto) throws BizException {
		AssertUtil.notNull(dto);

		try {
			// find user
			User user = new User();
			user.setId(0);

			// try to get model, used to create table
			modelService.trySelectModel(user, 0L);

			user.setUserName(dto.getUserName());

			List<Long> ids = queryService.SelectIDsByFieldName(user, "userName");

			if (ids == null || ids.isEmpty()) {
				throw new BizException("user not exists");
			}

			if (ids.size() > 1) {
				throw new BizException("duplicate usernames");
			}

			// get user detail
			modelService.selectModel(user, ids.get(0));

			// do some additional check, like locked

			// user found, update something
			user.setLastLoginTime(DateUtil.getNow());
			modelService.updateModel(user);

			// login ok, set value to dto
			dto.setUserID(user.getId());
			dto.setUserName(user.getUserName());
			dto.setPasswordHash(user.getPasswordHash());
			dto.setPasswordSalt(user.getPasswordSalt());

		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#Logout(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public void Logout(UserDTO dto) throws BizException {
		if (dto == null)
			return;

		try {
			// find user
			User user = new User();
			user.setUserName(dto.getUserName());

			List<Long> ids = queryService.SelectIDsByFieldName(user, "userName");

			if (ids == null || ids.isEmpty()) {
				throw new BizException("user not exists");
			}

			if (ids.size() > 1) {
				throw new BizException("duplicate usernames");
			}

			// get user detail
			modelService.selectModel(user, ids.get(0));

			// do some additional check, like locked

			// user found, update something
			user.setLastLogoutTime(DateUtil.getNow());
			modelService.updateModel(user);

		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

}
