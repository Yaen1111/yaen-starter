package org.yaen.starter.common.facade.services;

import org.yaen.starter.common.data.objects.Request;
import org.yaen.starter.common.data.objects.Response;

/**
 * hand shake service, for test purpose
 * 
 * @author Yaen 2015年11月30日下午11:49:06
 */
public interface HandshakeService {

	/**
	 * hand shake facade
	 * 
	 * @param req
	 * @return
	 */
	public Response Handshake(Request req);

}
