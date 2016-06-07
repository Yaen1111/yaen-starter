package org.yaen.starter.web.home.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.yaen.starter.common.util.contexts.MyX509TrustManager;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: WeiXinUtil
 * @Description: 微信公众接口工具类
 * @author huangwen
 * @regenerator huangwen
 * @date 2014-9-2 上午2:24:50
 * 
 */
public class WeiXinUtil {

	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 菜单创建（POST） 限100（次/天）
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	// 图文响应接口（POST） 限100（次/天）
	public static String pic_response_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	/**
	 * 发起https请求
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（Get或者post）
	 * @param outputStr
	 *            提交数据
	 * @return
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLcontext管理器对像，使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
			httpsUrlConn.setSSLSocketFactory(ssf);
			httpsUrlConn.setDoInput(true);
			httpsUrlConn.setDoOutput(true);
			httpsUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpsUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpsUrlConn.connect();
			}

			// 当有数据需要提交时
			if (outputStr != null) {
				OutputStream outputStream = httpsUrlConn.getOutputStream();
				// 防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
				outputStream = null;
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpsUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;

			httpsUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
			System.out.println(jsonObject);

		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

	public static AccessToken getAccessToken(String appid, String appsecret) {

		// 判断数据库的AccessToken是否超时，若未超时使用数据库的，若超时，则重新获取，并更新数据库

		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);

		// 如果请求成功
		if (jsonObject != null) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getString("expires_in"));
			} catch (Exception e) {
				// TODO: handle exception
				// TODO: handle exception
				accessToken = null;
				// 获取token失败
				log.error("获取token失败 errcode:{" + jsonObject.getInt("errcode") + "} errmsg:{"
						+ jsonObject.getString("errmsg") + "}");

			}
		}
		return accessToken;
	}

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		jsonMenu = jsonMenu.replaceAll(",null", "");
		// 调用接口创建菜单
		JSONObject jsonObject = httpsRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败  errcode:{" + jsonObject.getInt("errcode") + "} errmsg:{"
						+ jsonObject.getString("errmsg") + "}");
			}
		}

		return result;
	}

}