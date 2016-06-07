package org.yaen.starter.core.model.models.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.models.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * party attribute, simple key-value, 1:N
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZP_PARTY_ATTRIBUTE")
@OneIndex("ID")
public class PartyAttribute extends OneModel {
	private static final long serialVersionUID = 2310601189439211189L;

	/** the attribute name */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String attributeName;

	/** the attribute group */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String attributeGroup;

	/** the attribute value */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR250)
	private String attributeValue;

	/**
	 * empty constructor
	 */
	public PartyAttribute() {
		super();
	}
}
