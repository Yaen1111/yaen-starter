package org.yaen.starter.core.model.ark.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * system code model
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@Getter
@Setter
@OneTable(TableName = "ARK_SYSTEM_CODE")
public class SystemCodeEntity extends TwoEntity {
	private static final long serialVersionUID = 5112904110562141729L;

	/** the code, combined by 4 part */
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 6, FieldName = "CODE")
	private String code;

	/** the code type, E = error, W = warning, M = message, T = type enum */
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 1, FieldName = "CODE_TYPE")
	private String codeType;

	/** the system type, 0 = default/starter/common, 9 = user define */
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 1, FieldName = "SYSTEM_TYPE")
	private String systemType;

	/** the code group, can be letter */
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 2, FieldName = "CODE_GROUP")
	private String codeGroup;

	/** the code item, can be letter */
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 2, FieldName = "CODE_ITEM")
	private String codeItem;

	/** the title for read */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String title;

	/** the description for detail read */
	@OneData(DataType = DataTypes.VARCHAR256)
	private String description;

	/**
	 * format code
	 */
	protected void formatCode() {

		// trim
		this.codeType = StringUtil.trimToEmpty(this.codeType);
		this.systemType = StringUtil.trimToEmpty(this.systemType);
		this.codeGroup = StringUtil.trimToEmpty(this.codeGroup);
		this.codeItem = StringUtil.trimToEmpty(this.codeItem);

		// combine together
		this.code = this.codeType + this.systemType + this.codeGroup + this.codeItem;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#BeforeInsert(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeInsert() {
		if (super.BeforeInsert()) {

			// format code
			this.formatCode();

			return true;
		}
		return false;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#BeforeUpdate(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeUpdate() {
		if (super.BeforeUpdate()) {

			// format code
			this.formatCode();

			return true;
		}
		return false;
	}

}
