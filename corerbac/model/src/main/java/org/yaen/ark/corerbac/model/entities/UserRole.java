/**
 * 
 */
package org.yaen.ark.corerbac.model.entities;

import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.RelationElement;

import lombok.ToString;

/**
 * user role relation of rbac
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "RBAC_USER_ROLE")
public class UserRole extends RelationElement {

	/**
	 * constructor
	 */
	public UserRole(User user, Role role) {
		super(user, role);

	}

}
