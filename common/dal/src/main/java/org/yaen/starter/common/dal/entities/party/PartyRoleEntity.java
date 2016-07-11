package org.yaen.starter.common.dal.entities.party;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * party role, special form of attribute, 1:N
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@Getter
@Setter
@OneTable(TableName = "ZP_PARTY_ROLE")
@OneIndex("ID")
public class PartyRoleEntity extends TwoEntity {
	private static final long serialVersionUID = 1743340074473975732L;

	/** the party role type(code) */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String partyRoleType;

}
