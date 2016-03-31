/**
 * 
 */
package org.yaen.ark.corerbac.model.entities;

import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.BaseElement;
import org.yaen.ark.core.model.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * user of rbac, for party, can be any combination of party/role/relation/etc
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "RBAC_USER")
public class User extends BaseElement {

	/**
	 * the main user id, here is party id
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.BIGINT, FieldName = "PARTY_ID")
	private long partyId;

	/**
	 * the party role if any
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "PARTY_ROLE")
	private String partyRole;

	/**
	 * the to party id if in relationship, can be null
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.BIGINT, FieldName = "TO_PARTY_ID")
	private Long toPartyId;

	/**
	 * the relationship of two party
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "RELATIONSHIP")
	private String relationship;

	/**
	 * create user by party(or other account id) only
	 */
	public User(long partyId) {
		super();
		this.partyId = partyId;
	}

	/**
	 * create user by party and role
	 */
	public User(long partyId, String partyRole) {
		super();
		this.partyId = partyId;
		this.partyRole = partyRole;
	}

	/**
	 * create user by party and relationship
	 */
	public User(long partyId, String relationship, long toPartyId) {
		super();
		this.partyId = partyId;
		this.relationship = relationship;
		this.toPartyId = toPartyId;
	}

}
