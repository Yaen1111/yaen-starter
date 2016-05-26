/**
 * 
 */
package org.yaen.starter.core.model.one;

import org.yaen.starter.common.data.annotations.OneIgnore;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.models.BaseFractalModel;
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
public class OneFractalModel extends OneModel implements BaseFractalModel {
	private static final long serialVersionUID = 3470410424680588725L;

	/** the base model */
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
	public boolean BeforeSelect(ModelService service) throws CoreException {
		if (super.BeforeSelect(service)) {

			// need to select base
			service.selectModel(this.base, this.getId());

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeInsert()
	 */
	@Override
	public boolean BeforeInsert(ModelService service) throws CoreException {
		if (super.BeforeInsert(service)) {

			// insert parent if not
			if (this.base.getId() == 0) {
				service.insertModel(this.base);
				this.setId(this.base.getId());
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeUpdate(org.yaen.ModelService.common.services.EntityService)
	 */
	@Override
	public boolean BeforeUpdate(ModelService service) throws CoreException {
		if (super.BeforeUpdate(service)) {

			// update parent
			service.updateModel(base);

			return true;
		} else {
			return false;
		}
	}

}
