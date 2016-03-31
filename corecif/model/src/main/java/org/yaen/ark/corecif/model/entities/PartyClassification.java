/**
 * 
 */
package org.yaen.ark.corecif.model.entities;

import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.AttributeElement;
import org.yaen.ark.core.model.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * base party classification for party model
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_PARTY_CLASSIFICATION")
public class PartyClassification extends AttributeElement {

	/**
	 * the party role of the attribute, null means for party only
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "ROLE")
	private String role;

	/**
	 * constructor
	 */
	public PartyClassification(Party party) {
		super(party);

	}

}
