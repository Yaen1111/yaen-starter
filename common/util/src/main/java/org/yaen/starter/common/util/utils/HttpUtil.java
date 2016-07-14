package org.yaen.starter.common.util.utils;

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
import org.yaen.starter.common.util.contexts.X509AcceptAllTrustManager;

import com.alibaba.fastjson.JSONObject;

/**
 * http/https post/get util
 * 
 * @author Yaen 2016年5月11日下午1:53:39
 */
public class HttpUtil {

	/**
	 * http get and return result as string
	 * 
	 * @param requestUrl
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String httpGet(String requestUrl) throws ParseException, IOException {
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
	 * http post and return result as string
	 * 
	 * @param requestUrl
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static String httpPost(String requestUrl, Map<String, String> param) throws IOException {
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
	 * https request(get/set/head/delete), return response as json. the request url should be https://
	 * 
	 * @param requestUrl
	 * @param requestMethod
	 * @param dataString
	 * @return
	 * @throws Exception
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String dataString) throws Exception {

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
	 * https get
	 * 
	 * @param requestUrl
	 * @return
	 * @throws Exception
	 */
	public static String httpsGet(String requestUrl) throws Exception {
		return httpsRequest(requestUrl, "GET", null);
	}

	/**
	 * https post
	 * 
	 * @param requestUrl
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String httpsPost(String requestUrl, String dataString) throws Exception {
		return httpsRequest(requestUrl, "POST", dataString);
	}

	/**
	 * https post, treat param as json, result also as json
	 * 
	 * @param requestUrl
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static JSONObject httpsPostAsJson(String requestUrl, Map<String, Object> param) throws Exception {
		// convert param to json
		String dataString = null;
		if (param != null && !param.isEmpty()) {
			dataString = JSONObject.toJSONString(param);
		}

		String text = httpsPost(requestUrl, dataString);

		// convert result to json
		return JSONObject.parseObject(text);
	}

}
