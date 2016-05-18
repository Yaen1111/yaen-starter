package org.yaen.starter.biz.implement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.biz.shared.services.UserService;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.AssertUtil;
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

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#RegisterNewUser(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public long RegisterNewUser(UserDTO dto) throws Exception {
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
		long userid = modelService.insertModel(user);

		return userid;
	}

}
