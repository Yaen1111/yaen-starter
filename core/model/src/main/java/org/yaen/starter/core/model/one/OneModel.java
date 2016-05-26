/**
 * 
 */
package org.yaen.starter.core.model.one;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIgnore;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.DateUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * one model for base objects
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Slf4j
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
		} catch (ClassNotFoundException e) {
			// should not got here
			log.error("deep clone failed", e);
			return null;
		}

	}

	public boolean BeforeSelect(ModelService service) throws CoreException {
		return true;
	}

	public boolean AfterSelect(ModelService service) throws CoreException {
		return true;
	}

	public boolean BeforeInsert(ModelService service) throws CoreException {
		// set cdate, udate
		this.cdate = DateUtil.getNow();
		this.udate = this.cdate;

		return true;
	}

	public boolean AfterInsert(ModelService service) throws CoreException {
		return true;
	}

	public boolean BeforeUpdate(ModelService service) throws CoreException {
		// set udate date
		this.udate = DateUtil.getNow();

		return true;
	}

	public boolean AfterUpdate(ModelService service) throws CoreException {
		return true;
	}

	public boolean BeforeDelete(ModelService service) throws CoreException {
		return true;
	}

	public boolean AfterDelete(ModelService service) throws CoreException {
		return true;
	}

}
