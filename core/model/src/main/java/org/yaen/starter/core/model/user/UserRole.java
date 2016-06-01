package org.yaen.starter.core.model.user;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * relation of user and role
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@OneTable(TableName = "ZU_USER_ROLE")
@OneUniqueIndex("ID,ROLE_ID")
public class UserRole extends OneModel {
	private static final long serialVersionUID = -4458934767334916729L;

	/** the role id */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String roleId;

	/**
	 * constructor
	 */
	public UserRole() {
		super();
	}

}
