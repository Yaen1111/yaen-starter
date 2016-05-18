/**
 * 
 */
package org.yaen.starter.core.model.ark;

import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIgnore;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.core.model.one.OneModel;

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
public class Changelog extends OneModel {
	private static final long serialVersionUID = -606829448983274029L;

	/**
	 * the class of the internal model
	 */
	@OneIgnore
	private Class<?> clazz;

	/**
	 * the model before change, if create, null
	 */
	@Getter
	@Setter
	@OneCopy(Prefix = "BEFORE")
	private OneModel modelBefore;

	/**
	 * the model after change, if delete, null
	 */
	@Getter
	@Setter
	@OneCopy(Prefix = "AFTER")
	private OneModel modelAfter;

	/**
	 * the update user name
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "SYS_UUSER")
	private String uUser;

	/**
	 * the update comment
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 250, FieldName = "SYS_UCOMMENT")
	private String uComment;

	/**
	 * construct a changelog model, use deep copy for before
	 * 
	 * @param elementBefore
	 */
	public Changelog(Class<?> modelClass) {
		super();

		this.clazz = modelClass;
	}

	/**
	 * 
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeUpdate(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeUpdate(ModelService service) throws Exception {
		throw new CoreException("change document can not update");
	}

	/**
	 * 
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeDelete(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeDelete(ModelService service) throws Exception {
		throw new CoreException("change document can not delete");
	}

}
