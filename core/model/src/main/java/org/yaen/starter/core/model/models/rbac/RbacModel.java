package org.yaen.starter.core.model.models.rbac;

import org.yaen.starter.core.model.models.OneModel;

/**
 * rbac model
 * <p>
 * rbac is Role-Based Access Control.
 * <p>
 * is made up with user, role, auth, user-role, role-auth, extends user-auth and groups
 * <p>
 * especially suitable for authorization
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class RbacModel extends OneModel {

	/**
	 * empty constructor
	 */
	public RbacModel() {
		super("1.0.0");
	}
}
