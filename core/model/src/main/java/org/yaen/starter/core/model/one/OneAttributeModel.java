package org.yaen.starter.core.model.one;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIgnore;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.models.AttributeModel;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.AssertUtil;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * attribute of one model, 1:N
 * 
 * @author Yaen 2016年1月4日下午8:40:03
 */
@ToString(callSuper = true)
@OneTable(TableName = "ONE_ATTRIBUTE")
public class OneAttributeModel extends OneModel implements AttributeModel {
	private static final long serialVersionUID = -4376824168382204889L;

	/** the parent id */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@OneData(DataType = DataTypes.BIGINT, FieldName = "PARENT_ID")
	private long parentID;

	/** the parent model */
	@Getter
	@OneIgnore
	private OneModel parent;

	/**
	 * construct a attribute model
	 * 
	 * @param parent
	 */
	public OneAttributeModel(OneModel parent) {
		super();

		AssertUtil.notNull(parent);

		// set object
		this.parent = parent;

		// set id
		this.parentID = this.parent.getId();
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#AfterSelect(org.yaen.ModelService.common.services.EntityService)
	 */
	@Override
	public boolean AfterSelect(ModelService service) throws Exception {
		if (super.AfterSelect(service)) {

			// this maybe changed, reload parent
			if (this.parentID > 0) {
				service.selectModel(this.parent, this.parentID);
			}
			return true;
		} else {
			return false;
		}
	}

}
