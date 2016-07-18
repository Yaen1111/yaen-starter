package org.yaen.starter.core.model.entities.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * party relation, relation of 2 party
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@Getter
@Setter
@OneTable(TableName = "ZP_PARTY_RELATION")
@OneIndex("ID,TO_ID")
public class PartyRelationEntity extends TwoEntity {
	private static final long serialVersionUID = -4458934767334916729L;

	/** the party relation to id */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String toId;

	/** the party relation type(code) */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String partyRelationType;

}
