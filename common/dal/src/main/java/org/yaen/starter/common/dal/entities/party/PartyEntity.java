package org.yaen.starter.common.dal.entities.party;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * party main object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@Getter
@Setter
@OneTable(TableName = "ZP_PARTY")
@OneUniqueIndex("ID")
public class PartyEntity extends TwoEntity {
	private static final long serialVersionUID = -709733522935110043L;

	/** the party type, usually is org/person */
	@OneData(DataType = DataTypes.VARCHAR20)
	private String partyType;

	/** the reference resource, in some case, we may need the reference to other system */
	@OneData(DataType = DataTypes.VARCHAR50)
	private String referenceResource;

}
