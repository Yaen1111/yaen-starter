package org.yaen.starter.core.model.contexts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaen.starter.core.model.services.PartyService;

import lombok.Getter;

/**
 * service loader, used to get autowired service
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
@Component
public class ServiceLoader {

	/** the static party service */
	@Getter
	private static PartyService partyService;

	/**
	 * none-static setter with autowired
	 * 
	 * @param partyService
	 */
	@Autowired
	public void setPartyService(PartyService partyService) {
		ServiceLoader.partyService = partyService;
	}

}
