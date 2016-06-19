package org.yaen.starter.common.dal.entities.user;

import java.util.Date;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * user object, maybe other another model
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@Getter
@Setter
@OneTable(TableName = "ZU_USER")
@OneUniqueIndex("ID")
public class UserEntity extends TwoEntity {
	private static final long serialVersionUID = -709733522935110043L;

	/** password hashed by salt */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String passwordHash;

	/** password salt */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String passwordSalt;

	/** last login time, can be used for prevent multi-login */
	@OneData(DataType = DataTypes.DATETIME)
	private Date lastLoginTime;

	/** last logout time, can be used for prevent multi-login */
	@OneData(DataType = DataTypes.DATETIME)
	private Date lastLogoutTime;

}
