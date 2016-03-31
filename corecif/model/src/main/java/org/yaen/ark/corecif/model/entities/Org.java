/**
 * 
 */
package org.yaen.ark.corecif.model.entities;

import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.FractalElement;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.ark.corecif.model.enums.PartyTypes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * org shape of party
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_ORG")
public class Org extends FractalElement {

	@Getter
	private Party party;

	/**
	 * business license of org, usually is unique
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "BUSINESS_LICENSE")
	private String businessLicense;

	/**
	 * the full name of org, usually is unique
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 100, FieldName = "FULL_NAME")
	private String fullName;

	/**
	 * the short name of org
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 100, FieldName = "SHORT_NAME")
	private String shortName;

	/**
	 * create new org with party
	 * 
	 * @param party
	 */
	public Org(Party party) {
		super(party);

		this.party = party;
		this.setId(party.getId());
	}

	/**
	 * create new org
	 */
	public Org() {
		this(new Party());

		this.party.setPartyType(PartyTypes.ORG);
	}

}
