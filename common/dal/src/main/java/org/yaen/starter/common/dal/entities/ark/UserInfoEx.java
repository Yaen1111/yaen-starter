package org.yaen.starter.common.dal.entities.ark;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * user extend info, 1:1. if send to party, this is ignored
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@Getter
@Setter
@OneTable(TableName = "ZU_USER_INFO")
public class UserInfoEx extends TwoEntity {
	private static final long serialVersionUID = 8955391077702613155L;

	/** the true name of user */
	@OneData(DataType = DataTypes.VARCHAR50)
	private String trueName;

	/** the gender of user */
	@OneData(DataType = DataTypes.VARCHAR50)
	private String gender;

	/** mobile phone */
	@OneData(DataType = DataTypes.VARCHAR50)
	private String mobilePhone;

	/** fixed phone */
	@OneData(DataType = DataTypes.VARCHAR50)
	private String fixedPhone;

	/** email address, only one */
	@OneData(DataType = DataTypes.VARCHAR250)
	private String email;

	/** idcard type, usually is credential card */
	@OneData(DataType = DataTypes.VARCHAR20)
	private String idcardType;

	/** idcard number */
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 50)
	private String idcardNumber;

}
