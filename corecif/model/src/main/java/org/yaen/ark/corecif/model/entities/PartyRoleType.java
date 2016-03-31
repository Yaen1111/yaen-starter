/**
 * 
 */
package org.yaen.ark.corecif.model.entities;

import org.yaen.ark.core.model.NameElement;
import org.yaen.ark.core.model.annotations.ElementTable;

import lombok.ToString;

/**
 * base party role type definition for party model
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_PARTY_ROLE_TYPE")
public class PartyRoleType extends NameElement {

	/**
	 * constructor
	 */
	public PartyRoleType() {
		super();

	}

}
