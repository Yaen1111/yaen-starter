/**
 * 
 */
package org.yaen.ark.core.model.entities;

import org.yaen.spring.data.entities.BaseEntity;
import org.yaen.spring.data.entities.FractalEntity;
import org.yaen.spring.data.services.EntityService;
import org.yaen.spring.common.utils.AssertUtil;

import lombok.Getter;
import lombok.ToString;

/**
 * fractal element for elements, use container to hold parent info
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@ToString
public abstract class FractalElement extends BaseElement implements FractalEntity {

	@Getter
	private BaseEntity parent;

	/**
	 * fractal element
	 */
	public FractalElement(BaseEntity parent) {
		AssertUtil.notNull(parent);

		this.parent = parent;
		this.setId(parent.getId());
	}

	/**
	 * @see org.yaen.ark.core.model.entities.BaseElement#BeforeSelect(org.yaen.spring.common.services.EntityService)
	 */
	@Override
	public void BeforeSelect(EntityService service) throws Exception {
		super.BeforeSelect(service);

		// need to select parent
		service.selectEntity(this.parent, this.getId());
	}

	/**
	 * @see org.yaen.ark.core.model.entities.BaseElement#BeforeInsert()
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
	 * @see org.yaen.ark.core.model.entities.BaseElement#BeforeUpdate(org.yaen.spring.common.services.EntityService)
	 */
	@Override
	public void BeforeUpdate(EntityService service) throws Exception {
		super.BeforeUpdate(service);

		// update parent
		service.updateElement(parent);
	}

}
