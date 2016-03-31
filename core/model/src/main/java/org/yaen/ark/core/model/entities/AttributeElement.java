/**
 * 
 */
package org.yaen.ark.core.model.entities;

import org.yaen.spring.data.entities.AttributeEntity;
import org.yaen.spring.data.entities.BaseEntity;
import org.yaen.spring.data.services.EntityService;
import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.spring.common.utils.AssertUtil;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * attribute of one element with one element id, but may have many attributes
 * 
 * @author xl 2016年1月4日下午8:40:03
 */
@ToString(callSuper = true)
public abstract class AttributeElement extends BaseElement implements AttributeEntity {

	/**
	 * the base element id
	 */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@ElementData(DataType = DataTypes.BIGINT, FieldName = "BASE_ID")
	private long baseID;

	/**
	 * the base element
	 */
	@Getter
	private BaseEntity base;

	/**
	 * construct a attribute element
	 * 
	 * @param base
	 */
	public AttributeElement(BaseEntity base) {
		super();
		
		AssertUtil.notNull(base);

		// set object
		this.base = base;

		// set id
		this.baseID = this.base.getId();
	}

	/**
	 * @see org.yaen.ark.core.model.entities.BaseElement#AfterSelect(org.yaen.spring.common.services.EntityService)
	 */
	@Override
	public void AfterSelect(EntityService service) throws Exception {
		super.AfterSelect(service);

		if (this.baseID > 0) {
			service.selectEntity(this.base, this.baseID);
		}
	}

}
