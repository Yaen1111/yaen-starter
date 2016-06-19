package org.yaen.starter.common.dal.entities.rbac;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * relation of role and auth
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@Getter
@Setter
@OneTable(TableName = "ZU_ROLE_AUTH")
@OneUniqueIndex("ID,AUTH_ID")
public class RoleAuth extends TwoEntity {
	private static final long serialVersionUID = -4458934767334916729L;

	/** the auth id, should be long */
	@OneData(DataType = DataTypes.VARCHAR50)
	private String authId;

}
