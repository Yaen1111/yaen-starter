package org.yaen.starter.core.model.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * party role, special form of attribute, 1:N
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZP_PARTY_ROLE")
public class PartyRole extends OneModel {
	private static final long serialVersionUID = 1743340074473975732L;

	/** the party role type(code) */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String partyRoleType;

	/**
	 * empty constructor
	 */
	public PartyRole() {
		super();
	}
}
