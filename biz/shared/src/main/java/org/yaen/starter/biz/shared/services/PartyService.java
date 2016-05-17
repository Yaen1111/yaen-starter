package org.yaen.starter.biz.shared.services;

import org.yaen.starter.biz.shared.objects.PartyDTO;

/**
 * party service
 * 
 * @author Yaen 2016年5月17日下午3:58:01
 */
public interface PartyService {

	/**
	 * register new party with given info
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	long RegisterNewParty(PartyDTO dto) throws Exception;
}
