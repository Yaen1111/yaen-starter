/**
 * 
 */
package org.yaen.starter.core.model.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * party main object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@ToString(callSuper = true)
@OneTable(TableName = "PARTY")
public class Party extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/** the party type, usually is org/person */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "PARTY_TYPE")
	private String partyType;

	/** the reference resource, in some case, we may need the reference to other system */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 50, FieldName = "REFERENCE_RESOURCE")
	private String referenceResource;

	/**
	 * constructor
	 */
	public Party() {
		super();
	}
}
