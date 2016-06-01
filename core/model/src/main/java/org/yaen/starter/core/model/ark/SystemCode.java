/**
 * 
 */
package org.yaen.starter.core.model.ark;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * system code model
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@OneTable(TableName = "ARK_SYSTEM_CODE")
public class SystemCode extends OneModel {
	private static final long serialVersionUID = 5112904110562141729L;

	/**
	 * the code, combined by 4 part
	 */
	@Getter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 6, FieldName = "CODE")
	private String code;

	/**
	 * the code type, E = error, W = warning, M = message, T = type enum
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 1, FieldName = "CODE_TYPE")
	private String codeType;

	/**
	 * the system type, 0 = default/starter/common, 9 = user define
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 1, FieldName = "SYSTEM_TYPE")
	private String systemType;

	/**
	 * the code group, can be letter
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 2, FieldName = "CODE_GROUP")
	private String codeGroup;

	/**
	 * the code item, can be letter
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 2, FieldName = "CODE_ITEM")
	private String codeItem;

	/**
	 * the title for read
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String title;

	/**
	 * the description for detail read
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR250)
	private String description;

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeInsert(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeInsert() throws CoreException {
		if (super.BeforeInsert()) {

			// format code
			this.formatCode();

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeUpdate(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeUpdate() throws CoreException {
		if (super.BeforeUpdate()) {

			// format code
			this.formatCode();

			return true;
		} else {
			return false;
		}
	}

	/**
	 * format code
	 * 
	 * @throws CoreException
	 */
	protected void formatCode() throws CoreException {

		// trim
		this.codeType = StringUtil.trimToEmpty(this.codeType);
		this.systemType = StringUtil.trimToEmpty(this.systemType);
		this.codeGroup = StringUtil.trimToEmpty(this.codeGroup);
		this.codeItem = StringUtil.trimToEmpty(this.codeItem);

		// check length
		if (this.codeType.length() != 1 || this.systemType.length() != 1 || this.codeGroup.length() != 2
				|| this.codeItem.length() != 2) {
			throw new CoreException("code part not fit");
		}

		// combine together
		this.code = this.codeType + this.systemType + this.codeGroup + this.codeItem;
	}
}
