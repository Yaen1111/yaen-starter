package org.yaen.starter.core.model.party.models;

import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.user.entities.UserEntity;

/**
 * party model, can be used on any type of entity
 * <p>
 * party is made up with party, party-role, party-relationship, and extend with party-attribute
 * <p>
 * suitable for complex and relation-weighted authentication but not for authorization
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class PartyModel extends TwoModel {

	/**
	 * @param proxy
	 */
	public PartyModel(ProxyService proxy, OneEntity entity) {
		super(proxy, entity);
	}

	/**
	 * default party model with user entity
	 * 
	 * @param proxy
	 */
	public PartyModel(ProxyService proxy) {
		super(proxy, new UserEntity());
	}

}
