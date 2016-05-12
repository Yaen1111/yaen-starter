/**
 * 
 */
package org.yaen.starter.common.integration.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.data.objects.Request;
import org.yaen.starter.common.data.objects.Response;
import org.yaen.starter.common.facade.HandshakeService;
import org.yaen.starter.common.integration.HandshakeFacadeClient;

/**
 * facade client
 * 
 * @author Yaen 2015年12月2日下午10:58:19
 */
@Service
public class HandshakeFacadeClientImpl implements HandshakeFacadeClient {

	@Autowired
	HandshakeService handshakeFacade;

	/**
	 * facade handshake
	 * 
	 * @param req
	 * @return
	 */
	@Override
	public Response Handshake(Request req) {
		return handshakeFacade.Handshake(req);
	}

}
