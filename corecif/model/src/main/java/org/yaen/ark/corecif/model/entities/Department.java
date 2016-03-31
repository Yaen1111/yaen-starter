/**
 * 
 */
package org.yaen.ark.corecif.model.entities;

import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.FractalElement;
import org.yaen.ark.core.model.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * department shape of org
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_DEPARTMENT")
public class Department extends FractalElement {

	@Getter
	private Org org;

	/**
	 * the full name of org
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 100, FieldName = "FULL_NAME")
	private String fullName;

	/**
	 * create new department
	 * 
	 * @param party
	 */
	public Department(Org org) {
		super(org);

		this.org = org;
		this.setId(org.getId());
	}

	/**
	 * create new department
	 */
	public Department() {
		this(new Org());
	}

}
