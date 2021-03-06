package org.yaen.starter.core.model.entities;

import java.util.Date;

import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.IdUtil;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * two entity (follow of one entity), can be base entity suitable in most case
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
@Getter
@Setter
@OneTable(TableName = "TWO")
@OneUniqueIndex("ID")
public class TwoEntity extends OneEntity {
	private static final long serialVersionUID = 330177009269873369L;

	/** the create date time of the record */
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_CDATE")
	protected Date cdate;

	/** the last update date time of the record */
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_UDATE")
	protected Date udate;

	/**
	 * empty constructor
	 */
	public TwoEntity() {
		super();
	}

	/**
	 * constructor with id
	 * 
	 * @param id
	 */
	public TwoEntity(String id) {
		super();

		this.id = id;
	}

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
