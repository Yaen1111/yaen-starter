package org.yaen.starter.core.model.user;

import java.util.Date;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * user object, maybe other another model
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZU_USER")
public class User extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/** password salt */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 64)
	private String passwordSalt;

	/** password hashed by salt */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 64)
	private String passwordHash;

	/** last login time, can be used for prevent multi-login */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME)
	private Date lastLoginTime;

	/** last logout time, can be used for prevent multi-login */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME)
	private Date lastLogoutTime;

	/**
	 * constructor
	 */
	public User() {
		super();
	}
}
