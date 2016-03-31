/**
 * 
 */
package org.yaen.ark.core.model;


import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.entities.BaseElement;
import org.yaen.ark.core.model.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * code element, only for unique code/name
 * 
 * @author xl 2016年1月4日下午8:38:45
 */
@ToString(callSuper = true)
public abstract class NameElement extends BaseElement {

	/**
	 * the name
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String name;

	/**
	 * the family
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String family;

	/**
	 * the title
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 50)
	private String title;

	/**
	 * the description
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 250)
	private String description;
}
