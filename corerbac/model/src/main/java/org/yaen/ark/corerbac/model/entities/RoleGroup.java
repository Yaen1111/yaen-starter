/**
 * 
 */
package org.yaen.ark.corerbac.model.entities;

import org.yaen.ark.core.model.NameElement;
import org.yaen.ark.core.model.annotations.ElementTable;

import lombok.ToString;

/**
 * role group of rbac
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "RBAC_ROLE")
public class RoleGroup extends NameElement {

	/**
	 * constructor
	 */
	public RoleGroup() {
		super();

	}

}
