/**
 * 
 */
package org.yaen.ark.biz.implement;

import org.yaen.spring.common.facades.HandshakeFacade;
import org.yaen.spring.common.objects.Request;
import org.yaen.spring.common.objects.Response;

/**
 * 
 * 
 * @author xl 2015年12月1日上午12:18:15
 */
public class HandshakeService implements HandshakeFacade {

	/**
	 * @see org.yaen.arkcore.common.service.facade.HandshakeFacade#Handshake(org.yaen.arkcore.org.yaen.arkcommon.objects.data.CommonRequest)
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
