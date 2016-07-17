package org.yaen.starter.common.integration.clients.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.yaen.starter.common.integration.clients.HttpClient;
import org.yaen.starter.common.util.contexts.X509AcceptAllTrustManager;
import org.yaen.starter.common.util.utils.StringUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * http/https post/get client
 * 
 * @author Yaen 2016年5月11日下午1:53:39
 */
public class HttpClientImpl implements HttpClient {

	/**
	 * @see org.yaen.starter.common.integration.clients.HttpClient#httpGet(java.lang.String)
	 */
	@Override
	public String httpGet(String requestUrl) throws ParseException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(requestUrl);
			CloseableHttpResponse resp = httpclient.execute(httpget);
			return EntityUtils.toString(resp.getEntity());
		} finally {
			try {
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.HttpClient#httpPost(java.lang.String, java.util.Map)
	 */
	@Override
	public String httpPost(String requestUrl, Map<String, String> param) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(requestUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (param != null) {
				for (Entry<String, String> entry : param.entrySet()) {
					params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			HttpEntity formEntity = new UrlEncodedFormEntity(params);
			post.setEntity(formEntity);
			HttpResponse response = httpclient.execute(post);
			InputStream is = response.getEntity().getContent();
			return StringUtil.inputStream2String(is);
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.HttpClient#httpsRequest(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String httpsRequest(String requestUrl, String requestMethod, String dataString) throws Exception {

		// the result content
		StringBuilder sb = new StringBuilder();

		// connections
		HttpsURLConnection conn = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		try {
			// need trust manager for ssl, here we accept all
			TrustManager[] tm = { new X509AcceptAllTrustManager() };

			// init ssl context
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// get socket factory from ssl
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			// the url should be https, or the return will be URLConnection
			URL url = new URL(requestUrl);
			conn = (HttpsURLConnection) url.openConnection();

			// set attribute to the connection
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			// set method, get/post/head etc
			conn.setRequestMethod(requestMethod);

			// need for "GET" ??
			if (requestMethod.equalsIgnoreCase("GET"))
				conn.connect();

			// write output if not empty
			if (StringUtil.isNotBlank(dataString)) {
				OutputStream outputStream = conn.getOutputStream();

				// need utf-8
				outputStream.write(dataString.getBytes("UTF-8"));
				outputStream.close();
			}

			// get result
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;

			while ((str = bufferedReader.readLine()) != null) {
				sb.append(str);
			}

		} finally {

			// release
			if (bufferedReader != null) {
				bufferedReader.close();
				bufferedReader = null;
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
				inputStreamReader = null;
			}
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}

		return sb.toString();
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.HttpClient#httpsGet(java.lang.String)
	 */
	@Override
	public String httpsGet(String requestUrl) throws Exception {
		return this.httpsRequest(requestUrl, "GET", null);
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.HttpClient#httpsPost(java.lang.String, java.lang.String)
	 */
	@Override
	public String httpsPost(String requestUrl, String dataString) throws Exception {
		return this.httpsRequest(requestUrl, "POST", dataString);
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.HttpClient#httpsPostAsJson(java.lang.String, java.util.Map)
	 */
	@Override
	public JSONObject httpsPostAsJson(String requestUrl, Map<String, Object> param) throws Exception {
		// convert param to json
		String dataString = null;
		if (param != null && !param.isEmpty()) {
			dataString = JSONObject.toJSONString(param);
		}

		String text = this.httpsPost(requestUrl, dataString);

		// convert result to json
		return JSONObject.parseObject(text);
	}

}
