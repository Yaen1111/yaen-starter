/**
 * 
 */
package org.yaen.starter.core.model.one;

import org.springframework.util.Assert;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.models.AttributeModel;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.services.ModelService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * attribute of one element with one element id, but may have many attributes
 * 
 * @author Yaen 2016年1月4日下午8:40:03
 */
@ToString(callSuper = true)
public abstract class AttributeOne extends BaseOne implements AttributeModel {
	private static final long serialVersionUID = -4376824168382204889L;

	/**
	 * the base element id
	 */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@OneData(DataType = DataTypes.BIGINT, FieldName = "BASE_ID")
	private long baseID;

	/**
	 * the base element
	 */
	@Getter
	private BaseModel base;

	/**
	 * construct a attribute element
	 * 
	 * @param base
	 */
	public AttributeOne(BaseModel base) {
		super();

		Assert.notNull(base);

		// set object
		this.base = base;

		// set id
		this.baseID = this.base.getId();
	}

	/**
	 * @see org.yaen.starter.core.model.one.BaseOne#AfterSelect(org.yaen.ModelService.common.services.EntityService)
	 */
	@Override
	public void AfterSelect(ModelService service) throws Exception {
		super.AfterSelect(service);

		if (this.baseID > 0) {
			service.selectModel(this.base, this.baseID);
		}
	}

}
