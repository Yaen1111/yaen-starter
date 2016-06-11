package org.yaen.starter.core.model.models.user;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.models.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * relation of role and auth
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@OneTable(TableName = "ZU_ROLE_AUTH")
@OneUniqueIndex("ID,AUTH_ID")
public class RoleAuth extends OneModel {
	private static final long serialVersionUID = -4458934767334916729L;

	/** the auth id, should be long */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String authId;

	/**
	 * constructor
	 */
	public RoleAuth() {
		super();
	}

}