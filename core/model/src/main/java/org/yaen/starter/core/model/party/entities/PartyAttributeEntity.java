package org.yaen.starter.core.model.party.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

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
@OneIndex("ID,ATTRIBUTE_NAME")
public class PartyAttributeEntity extends TwoEntity {
	private static final long serialVersionUID = 2310601189439211189L;

	/** the attribute name */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String attributeName;

	/** the attribute group */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String attributeGroup;

	/** the attribute value */
	@OneData(DataType = DataTypes.VARCHAR256)
	private String attributeValue;

}
