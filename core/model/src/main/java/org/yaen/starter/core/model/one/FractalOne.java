/**
 * 
 */
package org.yaen.starter.core.model.one;

import org.springframework.util.Assert;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.models.FractalModel;
import org.yaen.starter.common.data.services.ModelService;

import lombok.Getter;
import lombok.ToString;

/**
 * fractal element for elements, use container to hold parent info
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@ToString
public abstract class FractalOne extends BaseOne implements FractalModel {
	private static final long serialVersionUID = 3470410424680588725L;
	
	@Getter
	private BaseModel parent;

	/**
	 * fractal element
	 */
	public FractalOne(BaseModel parent) {
		Assert.notNull(parent);

		this.parent = parent;
		this.setId(parent.getId());
	}

	/**
	 * @see org.yaen.starter.core.model.one.BaseOne#BeforeSelect(org.yaen.ModelService.common.services.EntityService)
	 */
	@Override
	public void BeforeSelect(ModelService service) throws Exception {
		super.BeforeSelect(service);

		// need to select parent
		service.selectModel(this.parent, this.getId());
	}

	/**
	 * @see org.yaen.starter.core.model.one.BaseOne#BeforeInsert()
	 */
	@Override
	public void BeforeInsert(ModelService service) throws Exception {
		super.BeforeInsert(service);

		// insert parent if not
		if (this.parent.getId() == 0) {
			service.insertModel(this.parent);
			this.setId(this.parent.getId());
		}
	}

	/**
	 * @see org.yaen.starter.core.model.one.BaseOne#BeforeUpdate(org.yaen.ModelService.common.services.EntityService)
	 */
	@Override
	public void BeforeUpdate(ModelService service) throws Exception {
		super.BeforeUpdate(service);

		// update parent
		service.updateModel(parent);
	}

}
