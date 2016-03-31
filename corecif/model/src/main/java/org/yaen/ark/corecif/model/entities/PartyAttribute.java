/**
 * 
 */
package org.yaen.ark.corecif.model.entities;

import java.util.Date;

import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.AttributeElement;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.spring.common.utils.DateUtil;
import org.yaen.spring.data.services.EntityService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * base party attriutes for party model
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_PARTY_ATTRIBUTE")
public class PartyAttribute extends AttributeElement {

	/**
	 * the party role of the attribute, null means for party only
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "ROLE")
	private String role;

	/**
	 * the party relationship date from
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.DATETIME, FieldName = "DATE_FROM")
	private Date dateFrom;

	/**
	 * the party relationship date to
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.DATETIME, FieldName = "DATE_TO")
	private Date dateTo;

	/**
	 * constructor
	 */
	public PartyAttribute(Party party) {
		super(party);

	}

	/**
	 * @see org.yaen.ark.core.model.BaseElement#BeforeInsert()
	 */
	@Override
	public void BeforeInsert(EntityService service) throws Exception {
		super.BeforeInsert(service);

		// set date from to now if not given
		if (this.dateFrom == null)
			this.dateFrom = DateUtil.getNow();

		// set date to to infinite if not given
		if (this.dateTo == null)
			this.dateTo = DateUtil.getInfinite();
	}
}
