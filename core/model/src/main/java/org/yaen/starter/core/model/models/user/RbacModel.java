package org.yaen.starter.core.model.models.user;

import java.util.List;

import org.yaen.starter.common.dal.entities.user.RoleEntity;
import org.yaen.starter.common.dal.entities.user.UserEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.contexts.ServiceLoader;
import org.yaen.starter.core.model.models.OneModel;

import lombok.Getter;

/**
 * rbac model
 * <p>
 * deal with rbac(Role-Based Access Control).
 * <p>
 * is made up with user, role, auth, user-role, role-auth, extends user-auth and groups
 * <p>
 * especially suitable for authorization
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class RbacModel extends OneModel {

	/** entity service */
	private EntityService entityService = ServiceLoader.getEntityService();

	/** query service */
	private QueryService queryService = ServiceLoader.getQueryService();

	/**
	 * empty constructor
	 */
	public RbacModel() {
		super("1.0.0");
	}

	/**
	 * create new role with given id
	 * 
	 * @param role
	 * @throws CoreException
	 * @throws DuplicateDataException
	 */
	public void createNewRole(RoleEntity role) throws CoreException, DuplicateDataException {
		AssertUtil.notNull(role);
		AssertUtil.notBlank(role.getId());

		try {

			// try get user by given username to check duplicate
			try {
				queryService.selectOneById(new RoleEntity(), role.getId());
			} catch (DuplicateDataException ex) {
				throw ex;
			} catch (DataNotExistsException e) {
				// demanded result, should be ok
			}

			// do insert
			entityService.insertEntityByRowid(role);

		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * select all role list
	 * 
	 * @return
	 * @throws CoreException
	 */
	public List<RoleEntity> getRoleListAll() throws CoreException {

		try {
			RoleEntity role = new RoleEntity();

			// select all
			List<Long> rowids = queryService.selectRowidsByAll(role);

			return queryService.selectListByRowids(role, rowids);

		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}

}
