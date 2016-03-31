/**
 * 
 */
package org.yaen.ark.core.model;

import java.util.Date;

import org.yaen.ark.core.model.annotations.ElementCopy;
import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.entities.BaseElement;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.spring.data.services.EntityService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * change log of element, save before and after value
 * 
 * @author xl 2016年1月4日下午8:40:03
 */
@ToString(callSuper = true)
@ElementTable(TableName = "ARK_CHANGELOG")
public class Changelog extends BaseElement {

	/**
	 * the class of the internal element
	 */
	private Class<?> clazz;

	/**
	 * the element before change, if create, null
	 */
	@Getter
	@Setter
	@ElementCopy(Prefix = "BEFORE")
	private BaseElement elementBefore;

	/**
	 * the element after change, if delete, null
	 */
	@Getter
	@Setter
	@ElementCopy(Prefix = "AFTER")
	private BaseElement elementAfter;

	/**
	 * the update date of the change document
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.DATETIME)
	private Date uDate;

	/**
	 * the update user name
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String uUser;

	/**
	 * the update comment
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 250)
	private String uComment;

	/**
	 * construct a changelog element, use deep copy for before
	 * 
	 * @param elementBefore
	 */
	public Changelog(Class<?> elementClass) {
		super();

		this.clazz = elementClass;
	}

	/**
	 * @see org.yaen.ark.core.model.elements.BaseElement#BeforeInsert()
	 */
	@Override
	public void BeforeInsert(EntityService service) throws Exception {
		super.BeforeInsert(service);

		// set update date
		this.uDate = new Date();
	}

	/**
	 * @throws Exception
	 * @see org.yaen.ark.core.model.elements.BaseElement#BeforeUpdate()
	 */
	@Override
	public void BeforeUpdate(EntityService service) throws Exception {
		throw new Exception("change document can not update");
	}

	/**
	 * @see org.yaen.ark.core.model.elements.BaseElement#BeforeDelete()
	 */
	@Override
	public void BeforeDelete(EntityService service) throws Exception {
		throw new Exception("change document can not delete");
	}

}
