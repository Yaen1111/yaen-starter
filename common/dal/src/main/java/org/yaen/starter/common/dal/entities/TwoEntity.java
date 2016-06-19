package org.yaen.starter.common.dal.entities;

import java.util.Date;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.IdUtil;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * another entity(persistent object) which can contain another object, which must implement BaseEntity and have an "id"
 * field of type long
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
@Getter
@Setter
public class TwoEntity extends OneEntity {
	private static final long serialVersionUID = 330177009269873369L;

	/** the create date time of the record */
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_CDATE")
	protected Date cdate;

	/** the last update date time of the record */
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_UDATE")
	protected Date udate;

	/** the last patch date time of the record */
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_PDATE")
	protected Date pdate;

	/** the patch log of the record, should use append */
	@OneData(DataType = DataTypes.TEXT, FieldName = "SYS_PLOG")
	protected String plog;

	/**
	 * @see org.yaen.starter.common.dal.entities.OneEntity#BeforeInsert()
	 */
	@Override
	public boolean BeforeInsert() {
		if (super.BeforeInsert()) {
			// set cdate, udate
			this.cdate = DateUtil.getNow();
			this.udate = this.cdate;

			// set id if not given
			if (StringUtil.isBlank(this.id)) {
				this.id = IdUtil.generateId("AUTO");
			}

			return true;
		}
		return false;
	}

	/**
	 * @see org.yaen.starter.common.dal.entities.OneEntity#BeforeUpdate()
	 */
	@Override
	public boolean BeforeUpdate() {
		if (super.BeforeUpdate()) {
			// set udate date
			this.udate = DateUtil.getNow();

			return true;
		}
		return false;
	}

}
