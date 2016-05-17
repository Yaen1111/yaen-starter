/**
 * 
 */
package org.yaen.starter.core.model.one;

import java.util.Date;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.Virtual;
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

	/**
	 * the primary key of id
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.BIGINT, FieldName = "ID")
	private long id;

	/**
	 * the create date time of the record
	 */
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

	/**
	 * the last patch date time of the record
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_PDATE")
	private Date pdate;

	/**
	 * the patch log of the record, should use append
	 */
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

	@Virtual
	public void BeforeSelect(ModelService service) throws Exception {
	}

	@Virtual
	public void AfterSelect(ModelService service) throws Exception {
	}

	@Virtual
	public void BeforeInsert(ModelService service) throws Exception {
		// set cdate, udate
		this.cdate = DateUtil.getNow();
		this.udate = this.cdate;
	}

	@Virtual
	public void AfterInsert(ModelService service) throws Exception {
	}

	@Virtual
	public void BeforeUpdate(ModelService service) throws Exception {
		// set udate date
		this.udate = DateUtil.getNow();
	}

	@Virtual
	public void AfterUpdate(ModelService service) throws Exception {
	}

	@Virtual
	public void BeforeDelete(ModelService service) throws Exception {
	}

	@Virtual
	public void AfterDelete(ModelService service) throws Exception {
	}

}
