package org.yaen.starter.common.data.facades;

import org.yaen.starter.common.data.objects.Request;
import org.yaen.starter.common.data.objects.Response;

/**
 * common service, mostly for test purpose
 * 
 * @author Yaen 2015年11月30日下午11:49:06
 */
public interface HandshakeFacade {

	/**
	 * hand shake service
	 * 
	 * @param req
	 * @return
	 */
	public Response Handshake(Request req);

}
