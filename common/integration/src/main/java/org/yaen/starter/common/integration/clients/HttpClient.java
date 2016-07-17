package org.yaen.starter.common.integration.clients;

import java.io.IOException;
import java.util.Map;

import org.apache.http.ParseException;

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
	 * @throws ParseException
	 * @throws IOException
	 */
	String httpGet(String requestUrl) throws ParseException, IOException;

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
	 * @throws Exception
	 */
	String httpsRequest(String requestUrl, String requestMethod, String dataString) throws Exception;

	/**
	 * https get
	 * 
	 * @param requestUrl
	 * @return
	 * @throws Exception
	 */
	String httpsGet(String requestUrl) throws Exception;

	/**
	 * https post
	 * 
	 * @param requestUrl
	 * @param dataString
	 * @return
	 * @throws Exception
	 */
	String httpsPost(String requestUrl, String dataString) throws Exception;

	/**
	 * https post, treat param as json, result also as json
	 * 
	 * @param requestUrl
	 * @param param
	 * @return
	 * @throws Exception
	 */
	JSONObject httpsPostAsJson(String requestUrl, Map<String, Object> param) throws Exception;

}
