package org.yaen.starter.core.model.party.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

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
@OneIndex("ID,PARTY_ROLE_TYPE")
public class PartyRoleEntity extends TwoEntity {
	private static final long serialVersionUID = 1743340074473975732L;

	/** the party role type(code) */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String partyRoleType;

}
