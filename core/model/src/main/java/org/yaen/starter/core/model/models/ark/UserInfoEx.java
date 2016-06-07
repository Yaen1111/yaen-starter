package org.yaen.starter.core.model.models.ark;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.models.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * user extend info, 1:1. if send to party, this is ignored
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZU_USER_INFO")
public class UserInfoEx extends OneModel {
	private static final long serialVersionUID = 8955391077702613155L;

	/** the true name of user */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String trueName;

	/** the gender of user */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String gender;

	/** mobile phone */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String mobilePhone;

	/** fixed phone */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String fixedPhone;

	/** email address, only one */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR250)
	private String email;

	/** idcard type, usually is credential card */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String idcardType;

	/** idcard number */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 50)
	private String idcardNumber;

	/**
	 * constructor
	 */
	public UserInfoEx() {
		super();
	}

}
