package org.yaen.starter.core.model.models.user;

import java.util.List;

import org.yaen.starter.common.dal.entities.user.UserEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.core.model.contexts.ServiceLoader;
import org.yaen.starter.core.model.models.OneModel;

import lombok.Getter;

/**
 * user model
 * <p>
 * user with rbac(Role-Based Access Control).
 * <p>
 * is made up with user, role, auth, user-role, role-auth, extends user-auth and groups
 * <p>
 * especially suitable for authorization
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class UserModel extends OneModel {

	/** the inner user */
	@Getter
	private UserEntity user;

	/** query service */
	private QueryService queryService = ServiceLoader.getQueryService();

	/**
	 * empty constructor
	 */
	public UserModel() {
		super("1.0.0");
	}

	/**
	 * load user by id
	 * 
	 * @param id
	 * @throws CoreException
	 * @throws DataException
	 */
	public void load(String id) throws CoreException, DataException {
		try {
			// get user by id
			List<UserEntity> list = queryService.selectListById(new UserEntity(), id);

			// check empty
			if (list == null || list.isEmpty()) {
				throw new DataNotExistsException("user not exists, id=" + id);
			}

			// check duplicate
			if (list.size() > 1) {
				throw new DuplicateDataException("user id duplicate, id=" + id);
			}

			this.user = list.get(0);

		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}
}
