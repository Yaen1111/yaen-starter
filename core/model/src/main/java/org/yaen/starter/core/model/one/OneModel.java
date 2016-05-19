/**
 * 
 */
package org.yaen.starter.core.model.one;

import java.util.Date;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIgnore;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.DateUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * one model for base objects
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@OneTable(TableName = "ONE")
public class OneModel implements BaseModel {
	private static final long serialVersionUID = 101L;

	/** enable change log or not, default to false */
	@Getter
	@Setter
	@OneIgnore
	private boolean enableChangeLog = false;

	/** the primary key of id */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.BIGINT, FieldName = "ID")
	private long id;

	/** the create date time of the record */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_CDATE")
	private Date cdate;

	/**
	 * the last update date time of the record
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_UDATE")
	private Date udate;

	/** the last patch date time of the record */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_PDATE")
	private Date pdate;

	/** the patch log of the record, should use append */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.TEXT, FieldName = "SYS_PLOG")
	private String plog;

	/**
	 * constructor
	 */
	public OneModel() {
		this.id = 0;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean BeforeSelect(ModelService service) throws Exception {
		return true;
	}

	public boolean AfterSelect(ModelService service) throws Exception {
		return true;
	}

	public boolean BeforeInsert(ModelService service) throws Exception {
		// set cdate, udate
		this.cdate = DateUtil.getNow();
		this.udate = this.cdate;

		return true;
	}

	public boolean AfterInsert(ModelService service) throws Exception {
		return true;
	}

	public boolean BeforeUpdate(ModelService service) throws Exception {
		// set udate date
		this.udate = DateUtil.getNow();

		return true;
	}

	public boolean AfterUpdate(ModelService service) throws Exception {
		return true;
	}

	public boolean BeforeDelete(ModelService service) throws Exception {
		return true;
	}

	public boolean AfterDelete(ModelService service) throws Exception {
		return true;
	}

}
