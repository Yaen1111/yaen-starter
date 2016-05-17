/**
 * 
 */
package org.yaen.starter.common.integration;

import org.yaen.starter.common.data.objects.Request;
import org.yaen.starter.common.data.objects.Response;

/**
 * client interface, must copy from server
 * 
 * @author Yaen 2016年5月12日下午2:01:42
 */
public interface HandshakeServiceClient {

	/**
	 * hand shake facade
	 * 
	 * @param req
	 * @return
	 */
	public Response Handshake(Request req);

}
