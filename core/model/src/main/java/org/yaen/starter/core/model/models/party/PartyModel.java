package org.yaen.starter.core.model.models.party;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.services.PartyService;

import lombok.Getter;

/**
 * party model
 * <p>
 * party is made up with party, party-role, party-relationship, and extend with party-attribute
 * <p>
 * suitable for complex and relation-weighted authentication but not for authorization
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class PartyModel extends OneModel {

	/** the service */
	@Getter
	private PartyService service;

	/**
	 * construct new model with service
	 * 
	 * @param service
	 */
	public PartyModel(PartyService service) {
		super("1.0.0");

		this.service = service;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#check()
	 */
	@Override
	public void check() throws CoreException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#clear()
	 */
	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}
}
