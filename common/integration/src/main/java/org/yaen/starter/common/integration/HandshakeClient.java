/**
 * 
 */
package org.yaen.starter.common.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.data.facades.HandshakeFacade;
import org.yaen.starter.common.data.objects.Request;
import org.yaen.starter.common.data.objects.Response;


/**
 * facade client
 * 
 * @author xl 2015年12月2日下午10:58:19
 */
@Service
public class HandshakeClient {

	@Autowired
	HandshakeFacade handshakeFacade;

	/**
	 * facade handshake
	 * 
	 * @param req
	 * @return
	 */
	public Response Handshake(Request req) {
		return handshakeFacade.Handshake(req);
	}

}
