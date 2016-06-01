package org.yaen.starter.core.model.user;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * relation of role and auth
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@OneTable(TableName = "ZU_ROLE_AUTH")
public class RoleAuth extends OneModel {
	private static final long serialVersionUID = -4458934767334916729L;

	/** the auth id */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String authId;

	/**
	 * constructor
	 */
	public RoleAuth() {
		super();
	}

}
