/**
 * 
 */
package org.yaen.ark.corecif.model.entities;

import org.yaen.ark.core.model.NameElement;
import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * base party relationship type for party model
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_PARTY_RELATIONSHIP")
public class PartyRelationshipType extends NameElement {

	/**
	 * the party role from
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "ROLE_FROM")
	private String roleFrom;

	/**
	 * the party role to
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "ROLE_TO")
	private String roleTo;

	/**
	 * constructor
	 */
	public PartyRelationshipType() {
		super();

	}

}
