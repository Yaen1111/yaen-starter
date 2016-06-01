package org.yaen.starter.core.model.one;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.IdUtil;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * one model for base objects, all member is protected
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@OneTable(TableName = "ONE")
public class OneModel implements BaseModel {
	private static final long serialVersionUID = 101L;

	/** the primary key of id */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.BIGINT, FieldName = "ROWID")
	protected long rowid = 0;

	/** the create date time of the record */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_CDATE")
	protected Date cdate;

	/**
	 * the last update date time of the record
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_UDATE")
	protected Date udate;

	/** the last patch date time of the record */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "SYS_PDATE")
	protected Date pdate;

	/** the patch log of the record, should use append */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.TEXT, FieldName = "SYS_PLOG")
	protected String plog;

	/** the main key of id */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20, FieldName = "ID")
	protected String id = "";

	/** enable change log or not, default to false */
	@Getter
	@Setter
	protected boolean enableChangeLog = false;

	/**
	 * empty constructor
	 */
	public OneModel() {
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @throws IOException
	 * @see org.yaen.starter.common.data.models.BaseModel#deepClone()
	 */
	@Override
	public Object deepClone() throws IOException {
		// serialize to stream
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(this);

		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bi);
		try {
			return oi.readObject();
		} catch (ClassNotFoundException ex) {
			// should not got here
			throw new RuntimeException(ex);
		}
	}

	/**
	 * return true
	 * 
	 * @see org.yaen.starter.common.data.models.BaseModel#BeforeSelect()
	 */
	@Override
	public boolean BeforeSelect() throws CoreException {
		return true;
	}

	/**
	 * empty
	 * 
	 * @see org.yaen.starter.common.data.models.BaseModel#AfterSelect()
	 */
	@Override
	public void AfterSelect() throws CoreException {
	}

	/**
	 * set cdate/udate/id if empty and return true
	 * 
	 * @see org.yaen.starter.common.data.models.BaseModel#BeforeInsert()
	 */
	@Override
	public boolean BeforeInsert() throws CoreException {
		// set cdate, udate
		this.cdate = DateUtil.getNow();
		this.udate = this.cdate;

		// set id if not given
		if (StringUtil.isBlank(this.id)) {
			this.id = IdUtil.generateId("AUTO");
		}

		return true;
	}

	/**
	 * empty
	 * 
	 * @see org.yaen.starter.common.data.models.BaseModel#AfterInsert()
	 */
	@Override
	public void AfterInsert() throws CoreException {
	}

	/**
	 * set udate and return true
	 * 
	 * @see org.yaen.starter.common.data.models.BaseModel#BeforeUpdate()
	 */
	@Override
	public boolean BeforeUpdate() throws CoreException {
		// set udate date
		this.udate = DateUtil.getNow();

		return true;
	}

	/**
	 * empty
	 * 
	 * @see org.yaen.starter.common.data.models.BaseModel#AfterUpdate()
	 */
	@Override
	public void AfterUpdate() throws CoreException {
	}

	/**
	 * return true
	 * 
	 * @see org.yaen.starter.common.data.models.BaseModel#BeforeDelete()
	 */
	@Override
	public boolean BeforeDelete() throws CoreException {
		return true;
	}

	/**
	 * empty
	 * 
	 * @see org.yaen.starter.common.data.models.BaseModel#AfterDelete()
	 */
	@Override
	public void AfterDelete() throws CoreException {
	}

}
