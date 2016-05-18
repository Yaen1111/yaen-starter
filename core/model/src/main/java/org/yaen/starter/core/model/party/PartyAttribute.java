/**
 * 
 */
package org.yaen.starter.core.model.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneAttributeModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * party attribute, 1:N
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@ToString(callSuper = true)
@OneTable(TableName = "PARTY_ATTRIBUTE")
public class PartyAttribute extends OneAttributeModel {
	private static final long serialVersionUID = 2310601189439211189L;

	/** the attribute1 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 50)
	private String attr1;

	/**
	 * constructor
	 */
	public PartyAttribute(Party party) {
		super(party);
	}
}
