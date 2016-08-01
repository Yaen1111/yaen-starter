package org.yaen.starter.common.integration.clients;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * http/https post/get client
 * 
 * @author Yaen 2016年5月11日下午1:53:39
 */
public interface HttpClient {

	/**
	 * http get and return result as string
	 * 
	 * @param requestUrl
	 * @return
	 * @throws IOException
	 */
	String httpGet(String requestUrl) throws IOException;

	/**
	 * 
	 * @param requestUrl
	 * @param dataString
	 * @return
	 * @throws IOException
	 */
	String httpPost(String requestUrl, String dataString) throws IOException;

	/**
	 * http post and return result as string
	 * 
	 * @param requestUrl
	 * @param param
	 * @return
	 * @throws IOException
	 */
	String httpPost(String requestUrl, Map<String, String> param) throws IOException;

	/**
	 * https request(get/post/head/delete), return response as json. the request url should be https://
	 * 
	 * @param requestUrl
	 * @param requestMethod
	 * @param dataString
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	String httpsRequest(String requestUrl, String requestMethod, String dataString)
			throws GeneralSecurityException, IOException;

	/**
	 * https get
	 * 
	 * @param requestUrl
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	String httpsGet(String requestUrl) throws GeneralSecurityException, IOException;

	/**
	 * https post
	 * 
	 * @param requestUrl
	 * @param dataString
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	String httpsPost(String requestUrl, String dataString) throws GeneralSecurityException, IOException;

	/**
	 * https post, treat param as json, result also as json
	 * 
	 * @param requestUrl
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	JSONObject httpsPostAsJson(String requestUrl, Map<String, Object> param)
			throws GeneralSecurityException, IOException;

}
