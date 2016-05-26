/**
 * 
 */
package org.yaen.starter.core.model.user;

import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.core.model.one.OneRelationModel;

import lombok.ToString;

/**
 * relation of user and role
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@ToString(callSuper = true)
@OneTable(TableName = "ZU_USER_ROLE")
public class UserRole extends OneRelationModel {
	private static final long serialVersionUID = -4458934767334916729L;

	/**
	 * constructor
	 * 
	 * @param user
	 * @param role
	 */
	public UserRole(User user, Role role) {
		super(user, role);
	}

}
