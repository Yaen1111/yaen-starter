/**
 * 
 */
package org.yaen.starter.core.model.user;

import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.core.model.one.OneModel;

import lombok.ToString;

/**
 * role object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@ToString(callSuper = true)
@OneTable(TableName = "ZU_ROLE")
public class Role extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/**
	 * constructor
	 */
	public Role() {
		super();
	}
}
