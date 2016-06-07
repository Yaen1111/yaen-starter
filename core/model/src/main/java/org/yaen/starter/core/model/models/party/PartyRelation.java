package org.yaen.starter.core.model.models.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.models.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * party relation, relation of 2 party
 * 
 * @author Yaen 2016年5月17日下午2:44:19
 */
@OneTable(TableName = "ZP_PARTY_RELATION")
@OneIndex("ID,TO_ID")
public class PartyRelation extends OneModel {
	private static final long serialVersionUID = -4458934767334916729L;

	/** the party relation to id */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String toId;

	/** the party relation type(code) */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String partyRelationType;

	/**
	 * empty constructor
	 */
	public PartyRelation() {
		super();
	}

}
