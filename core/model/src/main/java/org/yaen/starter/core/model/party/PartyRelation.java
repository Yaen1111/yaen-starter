/**
 * 
 */
package org.yaen.starter.core.model.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneRelationModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * party relation, relation of 2 party
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@ToString(callSuper = true)
@OneTable(TableName = "ZP_PARTY_RELATION")
public class PartyRelation extends OneRelationModel {
	private static final long serialVersionUID = -4458934767334916729L;

	/** the party relation type(code) */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20,  FieldName = "PARTY_RELATION_TYPE")
	private String partyRelationType;

	/**
	 * constructor
	 * 
	 * @param fromParty
	 * @param toParty
	 */
	public PartyRelation(Party fromParty, Party toParty) {
		super(fromParty, toParty);
	}

}
