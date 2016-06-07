package org.yaen.starter.biz.shared.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.biz.shared.objects.PartyDTO;
import org.yaen.starter.biz.shared.services.PartyService;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.party.Party;
import org.yaen.starter.core.model.models.party.PartyRole;

/**
 * 
 * @author Yaen 2016年5月17日下午4:08:26
 */
@Service
public class PartyServiceImpl implements PartyService {

	@Autowired
	private ModelService modelService;

	/**
	 * @see org.yaen.starter.biz.shared.services.PartyService#RegisterNewParty(org.yaen.starter.biz.shared.objects.PartyDTO)
	 */
	@Override
	public long RegisterNewParty(PartyDTO dto) throws Exception {
		AssertUtil.notNull(dto);

		// create new party
		Party party = new Party();

		party.setId(dto.getPartyID());
		party.setPartyType(dto.getPartyType());
		long partyid = modelService.insertModelByRowid(party);

		// create role if need
		if (StringUtil.isNotEmpty(dto.getPartyRoleType())) {
			PartyRole role = new PartyRole();
			role.setId(party.getId());
			role.setPartyRoleType(dto.getPartyRoleType());
			modelService.insertModelByRowid(role);
		}

		return partyid;
	}

}
