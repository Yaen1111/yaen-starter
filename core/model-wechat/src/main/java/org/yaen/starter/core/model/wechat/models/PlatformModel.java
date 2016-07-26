package org.yaen.starter.core.model.wechat.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformEntity;
import org.yaen.starter.core.model.wechat.utils.WechatJsonUtil;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;

/**
 * the wechat platform model(single use, will not refresh cache)
 * <p>
 * deals with most platform operations, only need access_token(both for platform or component-platform)
 * 
 * @author Yaen 2016年7月18日下午9:28:01
 */
public class PlatformModel extends TwoModel {

	@Override
	public PlatformEntity getEntity() {
		return (PlatformEntity) super.getEntity();
	}

	/** the current appid */
	@Getter
	protected String appid;

	/** the current access token */
	// @Getter
	protected String accessToken;

	/** is host or ad */
	@Getter
	protected boolean host;

	/**
	 * call api and check result
	 * 
	 * @param api
	 * @param param
	 * @return
	 * @throws CoreException
	 */
	protected JSONObject callApiPost(String api, Map<String, Object> param) throws CoreException {
		JSONObject json = null;
		try {
			json = this.proxy.getHttpClient().httpsPostAsJson(api, param);
		} catch (Exception ex) {
			throw new CoreException("call api failed", ex);
		}

		// check error
		WechatJsonUtil.checkErrCode(json);

		return json;
	}

	/**
	 * call api and check result
	 * 
	 * @param api
	 * @param param
	 * @return
	 * @throws CoreException
	 */
	protected JSONObject callApiGet(String api) throws CoreException {
		String res;
		try {
			res = this.proxy.getHttpClient().httpsGet(api);
		} catch (Exception ex) {
			throw new CoreException("call api failed", ex);
		}

		JSONObject json = JSONObject.parseObject(res);

		// check error
		WechatJsonUtil.checkErrCode(json);

		return json;
	}

	/**
	 * constructor for normal platform
	 * 
	 * @param proxy
	 */
	public PlatformModel(ProxyService proxy) {
		super(proxy, new PlatformEntity());

		this.appid = WechatPropertiesUtil.getAppid();

		this.host = true;
	}

	/**
	 * getter for accessToken
	 * 
	 * @return
	 * @throws CommonException
	 * @throws DataException
	 * @throws CoreException
	 */
	public String getAccessToken() throws DataException, CommonException, CoreException {
		final String API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		final String CACHE_KEY = "platform_access_token";

		Date now = DateUtil.getNow();
		Long nowtime = now.getTime() / 1000;

		// get access token for self

		// try to get from cache if null
		if (this.accessToken == null) {
			String access_token = this.readCache(CACHE_KEY);
			if (StringUtil.isNotBlank(access_token)) {
				this.accessToken = access_token;
			}
		}

		// try to load from db if null
		if (this.accessToken == null) {
			this.loadOrCreateById(this.appid);

			String access_token = this.getEntity().getAccessToken();
			Long create = this.getEntity().getAccessTokenCreate();
			Long expirein = this.getEntity().getAccessTokenExpireIn();

			// db is ok and not out of date
			if (StringUtil.isNotBlank(access_token) && create + expirein / 2 > nowtime) {
				// set cache
				this.writeCache(CACHE_KEY, access_token, (int) (nowtime - create - expirein / 2));
				// set member
				this.accessToken = access_token;
			}
		}

		// try to call api to get new one if null
		if (this.accessToken == null) {

			// call api
			JSONObject json = this.callApiGet(
					API.replace("APPID", this.appid).replace("APPSECRET", WechatPropertiesUtil.getSecret()));

			// get result value
			String access_token = json.getString("access_token");
			Long expires_in = json.getLong("expires_in");

			this.getEntity().setAccessToken(access_token);
			this.getEntity().setAccessTokenCreate(nowtime);
			this.getEntity().setAccessTokenExpireIn(expires_in);

			// set db
			this.saveById();
			// set cache
			this.writeCache(CACHE_KEY, access_token, expires_in.intValue());
			// set member
			this.accessToken = access_token;
		}

		// done
		return this.accessToken;
	}

	/**
	 * get user info json
	 * 
	 * @param openId
	 * @return
	 * @throws CommonException
	 * @throws DataException
	 * @throws CoreException
	 * @throws Exception
	 */
	public JSONObject getUserInfo(String openId) throws CoreException, DataException, CommonException {
		final String API = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

		// call api
		return this.callApiGet(API.replace("ACCESS_TOKEN", this.getAccessToken()).replace("OPENID", openId));
	}

	/**
	 * send custom message, only for service account
	 * 
	 * @param openid
	 * @param param
	 * @return
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	public JSONObject sendCustomMessage(String openid, Map<String, Object> param)
			throws CoreException, DataException, CommonException {
		final String API = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

		return this.callApiPost(API.replace("ACCESS_TOKEN", this.getAccessToken()), param);
	}

	/**
	 * send text message
	 * 
	 * <pre>
	 * {
	 * "touser":"OPENID",
	 * "msgtype":"text",
	 * "text":
	 * {
	 *      "content":"Hello World"
	 * }
	 * }
	 * </pre>
	 * 
	 * @param openid
	 * @param content
	 * @return
	 * @throws CommonException
	 * @throws DataException
	 * @throws CoreException
	 */
	public JSONObject sendCustomTextMessage(String openid, String content)
			throws CoreException, DataException, CommonException {
		// make param
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("touser", openid);
		param.put("msgtype", "text");

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("content", content);
		param.put("text", item);

		return this.sendCustomMessage(openid, param);
	}

	/**
	 * send news message
	 * 
	 * <pre>
	 * {
	 *     "touser":"OPENID",
	 *     "msgtype":"news",
	 *     "news":{
	 *         "articles": [
	 *          {
	 *              "title":"Happy Day",
	 *              "description":"Is Really A Happy Day",
	 *              "url":"URL",
	 *             "picurl":"PIC_URL"
	 *          },
	 *          {
	 *              "title":"Happy Day",
	 *              "description":"Is Really A Happy Day",
	 *              "url":"URL",
	 *              "picurl":"PIC_URL"
	 *          }
	 *          ]
	 *    }
	 * }
	 * </pre>
	 * 
	 * @param openid
	 * @param title
	 * @param description
	 * @param url
	 * @param picurl
	 * @return
	 * @throws CommonException
	 * @throws DataException
	 * @throws CoreException
	 */
	public JSONObject sendCustomNewsMessage(String openid, String title, String description, String url, String picurl)
			throws CoreException, DataException, CommonException {
		// make param
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("touser", openid);
		param.put("msgtype", "news");

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("title", title);
		item.put("description", description);
		item.put("url", url);
		item.put("picurl", picurl);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(item);

		Map<String, Object> articles = new HashMap<String, Object>();
		articles.put("articles", list);

		param.put("news", articles);

		return this.sendCustomMessage(openid, param);
	}
}
