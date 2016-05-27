package org.yaen.starter.web.home.utils;

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

import org.apache.commons.io.output.ByteArrayOutputStream;
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
import org.yaen.starter.common.util.contexts.MyX509TrustManager;
import org.yaen.starter.common.util.utils.StringUtil;

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
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String executeGet(String url) throws ParseException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);
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
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String executePost(String url, Map<String, String> param) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(url);
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
			return inStream2String(is);
		} finally {
			try {
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
			}
		}
	}

	// 将输入流转换成字符串
	private static String inStream2String(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		String str = new String(baos.toByteArray());
		baos.close();
		return str;
	}

	/**
	 * 发送https请求
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param param
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 * @throws Exception
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, Map<String, Object> param)
			throws Exception {
		JSONObject jsonObject = null;

		HttpsURLConnection conn = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			String params = "";
			if (param != null && !param.isEmpty()) {
				params = JSONObject.toJSONString(param);
			}

			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };

			// 目前针对SUN JVM,如果在IBM JVM下运行会报错，需要进行相应的调整
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);

			// 当outputStr不为null时向输出流写数据
			if (StringUtil.isNotBlank(params)) {
				OutputStream outputStream = conn.getOutputStream();

				// 注意编码格式
				outputStream.write(params.getBytes("UTF-8"));
				outputStream.close();
			}

			// 从输入流读取返回内容
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			jsonObject = (JSONObject) JSONObject.parse((buffer).toString());
		} finally {

			// 释放资源
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return jsonObject;
	}

	/**
	 * 发送https请求
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 * @throws Exception
	 */
	public static JSONObject httpsGetRequest(String requestUrl) throws Exception {
		return httpsRequest(requestUrl, "GET", null);
	}

	/**
	 * 发送https请求
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 * @throws Exception
	 */
	public static JSONObject httpsPostRequest(String requestUrl, Map<String, Object> param) throws Exception {
		return httpsRequest(requestUrl, "POST", param);
	}
}
