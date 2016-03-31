/**
 * 
 */
package org.yaen.ark.corerbac.model.entities;

import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.RelationElement;

import lombok.ToString;

/**
 * base party role for party model
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "RBAC_ROLE_AUTH")
public class RoleAuth extends RelationElement {

	/**
	 * constructor
	 */
	public RoleAuth(Role role, Auth auth) {
		super(role, auth);

	}

}
