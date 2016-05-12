/**
 * 
 */
package org.yaen.starter.biz.service.impl;

import org.springframework.stereotype.Service;
import org.yaen.starter.common.data.objects.Request;
import org.yaen.starter.common.data.objects.Response;
import org.yaen.starter.common.facade.HandshakeService;

/**
 * service implement
 * 
 * @author Yaen 2016年5月12日下午2:13:58
 */
@Service
public class HandshakeServiceImpl implements HandshakeService {

	/**
	 * 
	 * @see org.yaen.starter.common.facade.HandshakeService#Handshake(org.yaen.starter.common.data.objects.Request)
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
