/**
 * 
 */
package org.yaen.starter.core.model.user;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneFractalModel;
import org.yaen.starter.core.model.party.Party;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * user extend info, 1:1. if send to party, this is ignored
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@ToString(callSuper = true)
@OneTable(TableName = "USER_INFO_EX")
public class UserInfoEx extends OneFractalModel {
	private static final long serialVersionUID = 8955391077702613155L;

	/**
	 * the true name of user
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String trueName;

	/**
	 * the gender of user
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String gender;

	/**
	 * mobile phone
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String mobilePhone;

	/**
	 * fixed phone
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String fixedPhone;

	/**
	 * email address, only one
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 100)
	private String email;

	/**
	 * idcard type, usually is credential card
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String idcardType;

	/**
	 * idcard number
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String idcardNumber;

	/**
	 * constructor
	 */
	public UserInfoEx(Party party) {
		super(party);
	}

}
