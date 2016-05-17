/**
 * 
 */
package org.yaen.starter.core.model.one;

import org.yaen.starter.common.data.annotations.OneIgnore;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.models.FractalModel;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.AssertUtil;

import lombok.Getter;
import lombok.ToString;

/**
 * fractal part of model, 1:1
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@ToString(callSuper = true)
@OneTable(TableName = "ONE_FRACTAL")
public class OneFractalModel extends OneModel implements FractalModel {
	private static final long serialVersionUID = 3470410424680588725L;

	/**
	 * the base model
	 */
	@Getter
	@OneIgnore
	private OneModel base;

	/**
	 * constructor
	 * 
	 * @param base
	 */
	public OneFractalModel(OneModel base) {
		AssertUtil.notNull(base);

		this.base = base;
		this.setId(base.getId());
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeSelect(org.yaen.ModelService.common.services.EntityService)
	 */
	@Override
	public void BeforeSelect(ModelService service) throws Exception {
		super.BeforeSelect(service);

		// need to select base
		service.selectModel(this.base, this.getId());
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeInsert()
	 */
	@Override
	public void BeforeInsert(ModelService service) throws Exception {
		super.BeforeInsert(service);

		// insert parent if not
		if (this.base.getId() == 0) {
			service.insertModel(this.base);
			this.setId(this.base.getId());
		}
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeUpdate(org.yaen.ModelService.common.services.EntityService)
	 */
	@Override
	public void BeforeUpdate(ModelService service) throws Exception {
		super.BeforeUpdate(service);

		// update parent
		service.updateModel(base);
	}

}
