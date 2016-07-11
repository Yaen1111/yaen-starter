package org.yaen.starter.common.dal.entities.party;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * party attribute, simple key-value, 1:N
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@Getter
@Setter
@OneTable(TableName = "ZP_PARTY_ATTRIBUTE")
@OneIndex("ID")
public class PartyAttributeEntity extends TwoEntity {
	private static final long serialVersionUID = 2310601189439211189L;

	/** the attribute name */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String attributeName;

	/** the attribute group */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String attributeGroup;

	/** the attribute value */
	@OneData(DataType = DataTypes.VARCHAR250)
	private String attributeValue;

}
