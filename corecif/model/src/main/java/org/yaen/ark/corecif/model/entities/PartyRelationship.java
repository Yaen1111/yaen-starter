/**
 * 
 */
package org.yaen.ark.corecif.model.entities;

import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.RelationElement;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.spring.data.entities.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * base party relationship for party model
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_PARTY_RELATIONSHIP")
public class PartyRelationship extends RelationElement {

	/**
	 * the party relationship of two parties
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "RELATIONSHIP")
	private String relationship;

	/**
	 * constructor
	 */
	public PartyRelationship(BaseEntity fromEntity, BaseEntity toEntity) {
		super(fromEntity, toEntity);

	}

}
