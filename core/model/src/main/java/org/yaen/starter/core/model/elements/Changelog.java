/**
 * 
 */
package org.yaen.starter.core.model.elements;

import java.util.Date;

import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.services.EntityService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * change log of element, save before and after value
 * 
 * @author Yaen 2016年1月4日下午8:40:03
 */
@ToString(callSuper = true)
@OneTable(TableName = "ARK_CHANGELOG")
public class Changelog extends BaseElement {
	private static final long serialVersionUID = -606829448983274029L;

	/**
	 * the class of the internal element
	 */
	private Class<?> clazz;

	/**
	 * the element before change, if create, null
	 */
	@Getter
	@Setter
	@OneCopy(Prefix = "BEFORE")
	private BaseElement elementBefore;

	/**
	 * the element after change, if delete, null
	 */
	@Getter
	@Setter
	@OneCopy(Prefix = "AFTER")
	private BaseElement elementAfter;

	/**
	 * the update date of the change document
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME)
	private Date uDate;

	/**
	 * the update user name
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String uUser;

	/**
	 * the update comment
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 250)
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
	 * 
	 * @see org.yaen.starter.core.model.elements.BaseElement#BeforeInsert(org.yaen.starter.common.data.services.EntityService)
	 */
	@Override
	public void BeforeInsert(EntityService service) throws Exception {
		super.BeforeInsert(service);

		// set update date
		this.uDate = new Date();
	}

	/**
	 * 
	 * @see org.yaen.starter.core.model.elements.BaseElement#BeforeUpdate(org.yaen.starter.common.data.services.EntityService)
	 */
	@Override
	public void BeforeUpdate(EntityService service) throws Exception {
		throw new Exception("change document can not update");
	}

	/**
	 * 
	 * @see org.yaen.starter.core.model.elements.BaseElement#BeforeDelete(org.yaen.starter.common.data.services.EntityService)
	 */
	@Override
	public void BeforeDelete(EntityService service) throws Exception {
		throw new Exception("change document can not delete");
	}

}
