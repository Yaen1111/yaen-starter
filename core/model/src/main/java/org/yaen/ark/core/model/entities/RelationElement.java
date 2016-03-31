/**
 * 
 */
package org.yaen.ark.core.model.entities;

import java.util.Date;

import org.yaen.spring.data.entities.BaseEntity;
import org.yaen.spring.data.entities.RelationEntity;
import org.yaen.spring.data.services.EntityService;
import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.spring.common.utils.AssertUtil;
import org.yaen.spring.common.utils.DateUtil;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * relation of 2 element
 * 
 * @author xl 2016年1月4日下午8:40:03
 */
@ToString(callSuper = true)
public abstract class RelationElement extends BaseElement implements RelationEntity {

	/**
	 * the from element id
	 */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@ElementData(DataType = DataTypes.BIGINT, FieldName = "FROM_ID")
	private long fromId;

	/**
	 * the to element id
	 */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@ElementData(DataType = DataTypes.BIGINT, FieldName = "TO_ID")
	private long toId;

	/**
	 * the from entity, usually is the child
	 */
	@Getter
	private BaseEntity fromEntity;

	/**
	 * the to entity, usually is the parent
	 */
	@Getter
	private BaseEntity toEntity;

	/**
	 * the date from, if not set, use now
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.DATETIME, FieldName = "FROM_DATE")
	private Date fromDate;

	/**
	 * the date to, if not set, use infinite
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.DATETIME, FieldName = "TO_DATE")
	private Date toDate;

	/**
	 * construct a relation entity
	 * 
	 * @param fromEntity
	 * @param toEntity
	 */
	public RelationElement(BaseEntity fromEntity, BaseEntity toEntity) {
		super();

		AssertUtil.notNull(fromEntity);
		AssertUtil.notNull(toEntity);

		// set object
		this.fromEntity = fromEntity;
		this.toEntity = toEntity;

		// set id
		this.fromId = this.fromEntity.getId();
		this.toId = this.toEntity.getId();
	}

	/**
	 * @see org.yaen.ark.core.model.entities.BaseElement#AfterSelect(org.yaen.spring.common.services.EntityService)
	 */
	@Override
	public void AfterSelect(EntityService service) throws Exception {
		super.AfterSelect(service);

		// select element
		if (this.fromId > 0) {
			service.selectEntity(this.fromEntity, this.fromId);
		}

		if (this.toId > 0) {
			service.selectEntity(this.toEntity, this.toId);
		}
	}

	/**
	 * @see org.yaen.ark.core.model.entities.BaseElement#BeforeInsert()
	 */
	@Override
	public void BeforeInsert(EntityService service) throws Exception {
		super.BeforeInsert(service);

		// set date from to now if not given
		if (this.fromDate == null)
			this.fromDate = DateUtil.getNow();

		// set date to to infinite if not given
		if (this.toDate == null)
			this.toDate = DateUtil.getInfinite();
	}

}
