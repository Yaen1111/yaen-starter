package org.yaen.starter.common.dal.entities.user;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * relation of user and role
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@Getter
@Setter
@OneTable(TableName = "ZU_USER_ROLE")
@OneUniqueIndex("ID,ROLE_ID")
public class UserRoleEntity extends TwoEntity {
	private static final long serialVersionUID = -4458934767334916729L;

	/** the role id */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String roleId;

}
