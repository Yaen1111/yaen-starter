package org.yaen.starter.core.model.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * party main object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZP_PARTY")
@OneUniqueIndex("ID")
public class Party extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/** the party type, usually is org/person */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String partyType;

	/** the reference resource, in some case, we may need the reference to other system */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String referenceResource;

	/**
	 * empty constructor
	 */
	public Party() {
		super();
	}
}
