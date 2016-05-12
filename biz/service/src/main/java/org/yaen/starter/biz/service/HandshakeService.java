/**
 * 
 */
package org.yaen.starter.biz.service;

import org.yaen.starter.common.data.facades.HandshakeFacade;
import org.yaen.starter.common.data.objects.Request;
import org.yaen.starter.common.data.objects.Response;

/**
 * 
 * 
 * @author xl 2015年12月1日上午12:18:15
 */
public class HandshakeService implements HandshakeFacade {

	/**
	 * 
	 * @see org.yaen.starter.common.data.facades.HandshakeFacade#Handshake(org.yaen.starter.common.data.objects.Request)
	 */
	@Override
	public Response Handshake(Request req) {

		if (req == null)
			return null;

		Response res = new Response();
		res.setCode(req.getCode() + "+response");
		return res;
	}

}
