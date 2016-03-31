/**
 * 
 */
package org.yaen.ark.core.model.entities;

import java.util.Date;

import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.spring.common.annotations.Virtual;
import org.yaen.spring.data.entities.BaseEntity;
import org.yaen.spring.data.services.EntityService;
import org.yaen.spring.common.utils.DateUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * base element for elements
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString
public abstract class BaseElement implements BaseEntity {

	/**
	 * the primary key of id
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.BIGINT, FieldName = "ID")
	private long id;

	/**
	 * the create date time of the record
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.DATETIME, FieldName = "SYS_CDATE")
	private Date cdate;

	/**
	 * the last update date time of the record
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.DATETIME, FieldName = "SYS_UDATE")
	private Date udate;

	/**
	 * the last patch date time of the record
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.DATETIME, FieldName = "SYS_PDATE")
	private Date pdate;

	/**
	 * the patch log of the record, should use append
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.TEXT, FieldName = "SYS_PLOG")
	private Date plog;

	/**
	 * constructor
	 */
	public BaseElement() {
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
	public void BeforeSelect(EntityService service) throws Exception {
	}

	@Virtual
	public void AfterSelect(EntityService service) throws Exception {
	}

	@Virtual
	public void BeforeInsert(EntityService service) throws Exception {
		// set cdate, udate
		this.cdate = DateUtil.getNow();
		this.udate = this.cdate;
	}

	@Virtual
	public void AfterInsert(EntityService service) throws Exception {
	}

	@Virtual
	public void BeforeUpdate(EntityService service) throws Exception {
		// set udate
		this.udate = DateUtil.getNow();
	}

	@Virtual
	public void AfterUpdate(EntityService service) throws Exception {
	}

	@Virtual
	public void BeforeDelete(EntityService service) throws Exception {
	}

	@Virtual
	public void AfterDelete(EntityService service) throws Exception {
	}

}
