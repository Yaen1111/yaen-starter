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
 * employee shape of person
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@ElementTable(TableName = "CIF_EMPLOYEE")
public class Employee extends FractalElement {

	@Getter
	private Person person;

	/**
	 * the full name of person
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 100, FieldName = "FULL_NAME")
	private String fullName;

	/**
	 * create new employee
	 * 
	 * @param party
	 */
	public Employee(Person person) {
		super(person);

		this.person = person;
		this.setId(person.getId());
	}

	/**
	 * create enw employee
	 */
	public Employee() {
		this(new Person());
	}

}
