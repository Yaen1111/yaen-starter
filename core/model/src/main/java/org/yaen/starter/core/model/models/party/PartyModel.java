package org.yaen.starter.core.model.models.party;

import org.yaen.starter.common.dal.entities.party.PartyEntity;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;

/**
 * party model
 * <p>
 * party is made up with party, party-role, party-relationship, and extend with party-attribute
 * <p>
 * suitable for complex and relation-weighted authentication but not for authorization
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class PartyModel extends TwoModel<PartyEntity> {

	/**
	 * @param proxy
	 * @param sample
	 */
	public PartyModel(ProxyService proxy, PartyEntity sample) {
		super(proxy, sample);
	}

}
