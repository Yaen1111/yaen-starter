/**
 * 
 */
package org.yaen.starter.core.model.elements;

import org.springframework.util.Assert;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.entities.FractalEntity;
import org.yaen.starter.common.data.services.EntityService;

import lombok.Getter;
import lombok.ToString;

/**
 * fractal element for elements, use container to hold parent info
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@ToString
public abstract class FractalElement extends BaseElement implements FractalEntity {
	private static final long serialVersionUID = 3470410424680588725L;
	
	@Getter
	private BaseEntity parent;

	/**
	 * fractal element
	 */
	public FractalElement(BaseEntity parent) {
		Assert.notNull(parent);

		this.parent = parent;
		this.setId(parent.getId());
	}

	/**
	 * @see org.yaen.starter.core.model.elements.BaseElement#BeforeSelect(org.yaen.spring.common.services.EntityService)
	 */
	@Override
	public void BeforeSelect(EntityService service) throws Exception {
		super.BeforeSelect(service);

		// need to select parent
		service.selectEntity(this.parent, this.getId());
	}

	/**
	 * @see org.yaen.starter.core.model.elements.BaseElement#BeforeInsert()
	 */
	@Override
	public void BeforeInsert(EntityService service) throws Exception {
		super.BeforeInsert(service);

		// insert parent if not
		if (this.parent.getId() == 0) {
			service.insertEntity(this.parent);
			this.setId(this.parent.getId());
		}
	}

	/**
	 * @see org.yaen.starter.core.model.elements.BaseElement#BeforeUpdate(org.yaen.spring.common.services.EntityService)
	 */
	@Override
	public void BeforeUpdate(EntityService service) throws Exception {
		super.BeforeUpdate(service);

		// update parent
		service.updateEntity(parent);
	}

}
