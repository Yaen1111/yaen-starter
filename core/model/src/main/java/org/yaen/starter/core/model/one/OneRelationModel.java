/**
 * 
 */
package org.yaen.starter.core.model.one;

import java.util.Date;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIgnore;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.models.BaseRelationModel;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * relation of 2 element
 * 
 * @author Yaen 2016年1月4日下午8:40:03
 */
@ToString(callSuper = true)
@OneTable(TableName = "ONE_RELATION")
public class OneRelationModel extends OneModel implements BaseRelationModel {
	private static final long serialVersionUID = -8496063090342655991L;

	/** the from element id */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@OneData(DataType = DataTypes.BIGINT, FieldName = "FROM_ID")
	private long fromId;

	/** the to element id */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@OneData(DataType = DataTypes.BIGINT, FieldName = "TO_ID")
	private long toId;

	/** the from model, usually is the child */
	@Getter
	@OneIgnore
	private OneModel fromModel;

	/** the to model, usually is the parent */
	@Getter
	@OneIgnore
	private OneModel toModel;

	/** the date from, if not set, use now */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "FROM_DATE")
	private Date fromDate;

	/** the date to, if not set, use infinite */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME, FieldName = "TO_DATE")
	private Date toDate;

	/**
	 * construct a relation entity
	 * 
	 * @param fromModel
	 * @param toModel
	 */
	public OneRelationModel(OneModel fromModel, OneModel toModel) {
		super();

		AssertUtil.notNull(fromModel);
		AssertUtil.notNull(toModel);

		// set object
		this.fromModel = fromModel;
		this.toModel = toModel;

		// set id
		this.fromId = this.fromModel.getId();
		this.toId = this.toModel.getId();
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#AfterSelect(org.yaen.ModelService.common.services.EntityService)
	 */
	@Override
	public boolean AfterSelect(ModelService service) throws Exception {
		if (super.AfterSelect(service)) {

			// select element
			if (this.fromId > 0) {
				service.selectModel(this.fromModel, this.fromId);
			}

			if (this.toId > 0) {
				service.selectModel(this.toModel, this.toId);
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeInsert(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeInsert(ModelService service) throws Exception {
		if (super.BeforeInsert(service)) {

			// set date from to now if not given
			if (this.fromDate == null)
				this.fromDate = DateUtil.getNow();

			// set date to to infinite if not given
			if (this.toDate == null)
				this.toDate = DateUtil.getInfinite();

			return true;
		} else {
			return false;
		}
	}

}
