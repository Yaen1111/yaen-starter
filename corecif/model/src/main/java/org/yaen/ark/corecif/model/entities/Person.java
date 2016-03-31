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
 * person shape of party
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_PERSON")
public class Person extends FractalElement {

	@Getter
	private Party party;

	/**
	 * identity number of person, usually is unique
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "IDENTITY_NUMBER")
	private String identityNumber;

	/**
	 * true name of person
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "TRUE_NAME")
	private String trueName;

	/**
	 * used name of person, maybe list
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 100, FieldName = "USED_NAME")
	private String usedName;

	/**
	 * create new person with party
	 * 
	 * @param party
	 */
	public Person(Party party) {
		super(party);

		this.party = party;
		this.setId(party.getId());
	}

	/**
	 * create new person
	 */
	public Person() {
		this(new Party());

		this.party.setPartyType(PartyTypes.PERSON);
	}

}
