package org.yaen.starter.core.model.wechat.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.ParseUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.ComponentEntity;
import org.yaen.starter.core.model.wechat.entities.PlatformEntity;
import org.yaen.starter.core.model.wechat.utils.WechatJsonUtil;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * the wechat component model, for 3rd-party open platform
 * <p>
 * the most important difference is the method to get access_token
 * 
 * @author Yaen 2016年7月18日下午9:28:07
 */
@Slf4j
public class ComponentModel extends TwoModel {

	@Override
	public ComponentEntity getEntity() {
		return (ComponentEntity) super.getEntity();
	}

	/** the appid from config */
	@Getter
	private String componentAppid;

	/** the component access token, can be cached */
	// @Getter
	private String componentAccessToken;

	/** the pre auth code for platform to auth the component */
	// @Getter
	private String preAuthCode;

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
			log.debug("callApiPost, api={}, param={}", api, param);

			json = this.proxy.getHttpClient().httpsPostAsJson(api, param);

			log.debug("callApiPost, result={}", json);

		} catch (Exception ex) {
			throw new CoreException("call api failed", ex);
		}

		// check error
		WechatJsonUtil.checkErrCode(json);

		return json;
	}

	/**
	 * constructor
	 * 
	 * @param proxy
	 */
	public ComponentModel(ProxyService proxy) {
		super(proxy, new ComponentEntity());

		// get appid
		this.componentAppid = WechatPropertiesUtil.getComponentAppid();
	}

	/**
	 * step 2: get component_access_token
	 * 
	 * <pre>
	 * request post body:
	 * {
	 * "component_appid":"appid_value" ,
	 * "component_appsecret": "appsecret_value", 
	 * "component_verify_ticket": "ticket_value" 
	 * }
	 * </pre>
	 * 
	 * <pre>
	 * response body:
	 * {
	 * "component_access_token":"61W3mEpU66027wgNZ_MhGHNQDHnFATkDa9-2llqrMBjUwxRSNPbVsMmyD-yq8wZETSoE5NQgecigDrSHkPtIYA", 
	 * "expires_in":7200
	 * }
	 * </pre>
	 * 
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	public String getComponentAccessToken() throws CoreException, DataException, CommonException {
		final String API = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
		final String CACHE_KEY = "component_access_token";

		Date now = DateUtil.getNow();
		Long nowtime = now.getTime() / 1000;

		// try to get from cache if null
		if (this.componentAccessToken == null) {
			String component_access_token = this.readCache(CACHE_KEY);
			if (StringUtil.isNotBlank(component_access_token)) {
				this.componentAccessToken = component_access_token;
			}
		}

		// try to load from db if null
		if (this.componentAccessToken == null) {
			this.loadOrCreateById(this.componentAppid);

			String component_access_token = this.getEntity().getComponentAccessToken();
			Long create = this.getEntity().getComponentAccessTokenCreate();
			Long expirein = this.getEntity().getComponentAccessTokenExpireIn();

			// db is ok and not out of date
			if (StringUtil.isNotBlank(component_access_token) && create + expirein / 2 > nowtime) {
				// set cache
				this.writeCache(CACHE_KEY, component_access_token, (int) (nowtime - create - expirein / 2));
				// set member
				this.componentAccessToken = component_access_token;
			}
		}

		// try to call api to get new one if null
		if (this.componentAccessToken == null) {
			// get ticket, whitch is send by wechat server every 10 minutes
			String component_verify_ticket = this.getEntity().getComponentVerifyTicket();

			// make param value
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("component_appid", this.componentAppid);
			param.put("component_appsecret", WechatPropertiesUtil.getComponentSecret());
			param.put("component_verify_ticket", component_verify_ticket);

			// call api
			JSONObject json = this.callApiPost(API, param);

			// get result value
			String component_access_token = json.getString("component_access_token");
			Long expires_in = json.getLong("expires_in");

			this.getEntity().setComponentAccessToken(component_access_token);
			this.getEntity().setComponentAccessTokenCreate(nowtime);
			this.getEntity().setComponentAccessTokenExpireIn(expires_in);

			// set db
			this.saveById();
			// set cache
			this.writeCache(CACHE_KEY, component_access_token, expires_in.intValue());
			// set member
			this.componentAccessToken = component_access_token;
		}

		// done
		return this.componentAccessToken;
	}

	/**
	 * step 3: get pre_auth_code
	 * 
	 * <pre>
	 * request post body:
	 * {
	 * "component_appid":"appid_value" 
	 * }
	 * </pre>
	 * 
	 * <pre>
	 * response body:
	 * {
	 * "pre_auth_code":"Cx_Dk6qiBE0Dmx4EmlT3oRfArPvwSQ-oa3NL_fwHM7VI08r52wazoZX2Rhpz1dEw",
	 * "expires_in":600
	 * }
	 * </pre>
	 * 
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	public String getPreAuthCode() throws CoreException, DataException, CommonException {
		final String API = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
		final String CACHE_KEY = "pre_auth_code";

		Date now = DateUtil.getNow();
		Long nowtime = now.getTime() / 1000;

		// try to get from cache if null
		if (this.preAuthCode == null) {
			String pre_auth_code = this.readCache(CACHE_KEY);
			if (StringUtil.isNotBlank(pre_auth_code)) {
				this.preAuthCode = pre_auth_code;
			}
		}

		// try to load from db if null
		if (this.preAuthCode == null) {
			this.loadOrCreateById(this.componentAppid);

			String pre_auth_code = this.getEntity().getPreAuthCode();
			Long create = this.getEntity().getPreAuthCodeCreate();
			Long expirein = this.getEntity().getPreAuthCodeExpireIn();

			// db is ok and not out of date
			if (StringUtil.isNotBlank(pre_auth_code) && create + expirein / 2 > nowtime) {
				// set cache
				this.writeCache(CACHE_KEY, pre_auth_code, (int) (nowtime - create - expirein / 2));
				// set member
				this.preAuthCode = pre_auth_code;
			}
		}

		// try to call api to get new one if null
		if (this.preAuthCode == null) {
			// make param value
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("component_appid", this.componentAppid);

			// call api
			JSONObject json = this.callApiPost(API.replace("COMPONENT_ACCESS_TOKEN", this.getComponentAccessToken()),
					param);

			// get result value
			String pre_auth_code = json.getString("pre_auth_code");
			Long expires_in = json.getLong("expires_in");

			this.getEntity().setPreAuthCode(pre_auth_code);
			this.getEntity().setPreAuthCodeCreate(nowtime);
			this.getEntity().setPreAuthCodeExpireIn(expires_in);

			// set db
			this.saveById();
			// set cache
			this.writeCache(CACHE_KEY, pre_auth_code, expires_in.intValue());
			// set member
			this.preAuthCode = pre_auth_code;
		}

		// done
		return this.preAuthCode;
	}

	/**
	 * step 4: get authorization_info (api call only, no cache, no db, return api result json)
	 * 
	 * <pre>
	 * request post body:
	 * {
	 * "component_appid":"appid_value" ,
	 * "authorization_code": "auth_code_value"
	 * }
	 * </pre>
	 * 
	 * <pre>
	 * response body:
	 * { 
	 * "authorization_info": {
	 * "authorizer_appid": "wxf8b4f85f3a794e77", 
	 * "authorizer_access_token": "QXjUqNqfYVH0yBE1iI_7vuN_9gQbpjfK7hYwJ3P7xOa88a89-Aga5x1NMYJyB8G2yKt1KCl0nPC3W9GJzw0Zzq_dBxc8pxIGUNi_bFes0qM", 
	 * "expires_in": 7200, 
	 * "authorizer_refresh_token": "dTo-YCXPL4llX-u1W1pPpnp8Hgm4wpJtlR6iV0doKdY", 
	 * "func_info": [
	 * {
	 * "funcscope_category": {
	 * "id": 1
	 * }
	 * }, 
	 * {
	 * "funcscope_category": {
	 * "id": 2
	 * }
	 * }, 
	 * {
	 * "funcscope_category": {
	 * "id": 3
	 * }
	 * }
	 * ]
	 * }
	 * </pre>
	 * 
	 * @param authorizationCode
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	public JSONObject getAuthorizationInfoApi(String authorizationCode)
			throws CoreException, DataException, CommonException {
		final String API = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=COMPONENT_ACCESS_TOKEN";

		AssertUtil.notBlank(authorizationCode);

		// make param value
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("component_appid", this.componentAppid);
		param.put("authorization_code", authorizationCode);

		// call api
		JSONObject json = this.callApiPost(API.replace("COMPONENT_ACCESS_TOKEN", this.getComponentAccessToken()),
				param);

		// done
		return json;
	}

	/**
	 * step 5: get refresh_access_token (api call only, no cache, no db, return api result json)
	 * 
	 * <pre>
	 * request post body:
	 * {
	 * "component_appid":"appid_value",
	 * "authorizer_appid":"auth_appid_value",
	 * "authorizer_refresh_token":"refresh_token_value",
	 * }
	 * </pre>
	 * 
	 * <pre>
	 * response body:
	 * {
	 * "authorizer_access_token": "aaUl5s6kAByLwgV0BhXNuIFFUqfrR8vTATsoSHukcIGqJgrc4KmMJ-JlKoC_-NKCLBvuU1cWPv4vDcLN8Z0pn5I45mpATruU0b51hzeT1f8", 
	 * "expires_in": 7200, 
	 * "authorizer_refresh_token": "BstnRqgTJBXb9N2aJq6L5hzfJwP406tpfahQeLNxX0w"
	 * }
	 * </pre>
	 * 
	 * @param appid
	 * @param refreshToken
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	public JSONObject refreshAccessTokenApi(String appid, String refreshToken)
			throws CoreException, DataException, CommonException {
		final String API = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";

		AssertUtil.notBlank(appid);
		AssertUtil.notBlank(refreshToken);

		// make param value
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("component_appid", this.componentAppid);
		param.put("authorizer_appid", appid);
		param.put("authorizer_refresh_token", refreshToken);

		// call api
		JSONObject json = this.callApiPost(API.replace("COMPONENT_ACCESS_TOKEN", this.getComponentAccessToken()),
				param);

		// done
		return json;
	}

	/**
	 * step 6: get authorizer_info (the platform binded to the component)
	 * 
	 * <pre>
	 * request post body:
	 * {
	 * "component_appid":"appid_value" ,
	 * "authorizer_appid": "auth_appid_value" 
	 * }
	 * </pre>
	 * 
	 * <pre>
	 * response body:
	 * {
	 * "authorizer_info": {
	 * "nick_name": "微信SDK Demo Special", 
	 * "head_img": "http://wx.qlogo.cn/mmopen/GPyw0pGicibl5Eda4GmSSbTguhjg9LZjumHmVjybjiaQXnE9XrXEts6ny9Uv4Fk6hOScWRDibq1fI0WOkSaAjaecNTict3n6EjJaC/0", 
	 * "service_type_info": { "id": 2 }, 
	 * "verify_type_info": { "id": 0 },
	 * "user_name":"gh_eb5e3a772040",
	 * "business_info": {"open_store": 0, "open_scan": 0, "open_pay": 0, "open_card": 0, "open_shake": 0},
	 * "alias":"paytest01"
	 * }, 
	 * "qrcode_url":"URL",    
	 * "authorization_info": {
	 * "appid": "wxf8b4f85f3a794e77", 
	 * "func_info": [
	 * { "funcscope_category": { "id": 1 } }, 
	 * { "funcscope_category": { "id": 2 } }, 
	 * { "funcscope_category": { "id": 3 } }
	 * ]
	 * }
	 * }
	 * </pre>
	 * 
	 * @param appid
	 * @return
	 * @throws CommonException
	 * @throws DataException
	 * @throws CoreException
	 */
	public JSONObject getAuthorizerInfoApi(String appid) throws CoreException, DataException, CommonException {
		final String API = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";

		AssertUtil.notBlank(appid);

		// make param value
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("component_appid", this.componentAppid);
		param.put("authorizer_appid", appid);

		// call api
		JSONObject json = this.callApiPost(API.replace("COMPONENT_ACCESS_TOKEN", this.getComponentAccessToken()),
				param);

		// done
		return json;
	}

	/**
	 * get platform model from current component by given appid, maybe self platform or platform component
	 * <p>
	 * if authInfo not set, will get from server if need
	 * 
	 * @param appid
	 * @param authInfo
	 * @param auth_code
	 * @param expires_in
	 * @return
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	public PlatformModel getPlatformModel(String appid, JSONObject authInfo, String auth_code, String expires_in)
			throws CoreException, DataException, CommonException {
		AssertUtil.notBlank(appid);

		// if the appid is self, just return new platform
		if (StringUtil.equals(appid, WechatPropertiesUtil.getAppid())) {
			return new PlatformModel(this.proxy);
		}

		Date now = DateUtil.getNow();
		Long nowtime = now.getTime() / 1000;

		// find temp platform from db
		PlatformModel tempPlatform = new PlatformModel(this.proxy);
		tempPlatform.loadOrCreateById(appid);

		// get entity
		PlatformEntity platform_entity = tempPlatform.getEntity();

		// set auth code if given
		if (StringUtil.isNotBlank(auth_code)) {
			platform_entity.setAuthorizationCode(auth_code);
			platform_entity.setAuthorizationCodeExpiredTime(nowtime + ParseUtil.tryParseLong(expires_in, 7200L));

			// unset refresh token
			platform_entity.setRefreshToken(null);
		}

		// the local auth info
		JSONObject auth_info = authInfo;

		// need refresh token, if null, call api to get one
		if (StringUtil.isBlank(platform_entity.getRefreshToken())) {
			// get refresh token, need auth code
			if (StringUtil.isBlank(platform_entity.getAuthorizationCode())
					|| platform_entity.getAuthorizationCodeExpiredTime() <= nowtime) {
				throw new CoreException(
						"the platform has no refresh token nor authorization code(or expired), must be re-authorized. appid="
								+ appid);
			}

			// call api to get auth code
			if (auth_info == null) {
				auth_info = this.getAuthorizationInfoApi(platform_entity.getAuthorizationCode());
			}

		}

		// update by auth info, given or new get
		if (auth_info != null) {
			// get root info
			JSONObject info = auth_info.getJSONObject("authorization_info");

			// check appid
			String authorizer_appid = info.getString("authorizer_appid");
			if (!StringUtil.equals(authorizer_appid, appid)) {
				throw new CoreException(
						"the auth code of appid is not same with the given appid, maybe authcode assign error, please check and re-authorize. "
								+ "authorized appid=" + authorizer_appid + ", appid=" + appid);
			}

			// set to entity
			platform_entity.setAccessToken(info.getString("authorizer_access_token"));
			platform_entity.setAccessTokenCreate(nowtime);
			platform_entity.setAccessTokenExpireIn(Long.parseLong(info.getString("expires_in")));
			platform_entity.setRefreshToken(info.getString("authorizer_refresh_token"));
			platform_entity.setRefreshTokenCreate(nowtime);

			// make func scope list
			// JSONArray arr = json.getJSONArray("func_info");
			// for(int i = 0; i<arr.size() ;i++){
			// arr.getJSONObject(0);
			// }

			// save to db
			tempPlatform.saveById();
		}

		// need to get access token, if null, call api to get new one, if expired, call api to refresh
		if (StringUtil.isBlank(platform_entity.getAccessToken())
				|| platform_entity.getAccessTokenCreate() + platform_entity.getAccessTokenExpireIn() / 2 <= nowtime) {

			// need refresh token
			if (StringUtil.isBlank(platform_entity.getRefreshToken())) {
				throw new CoreException(
						"the platform has access token nor refresh token(or expired), must be re-authorized. appid="
								+ appid);
			}

			// call api to refresh token
			JSONObject json = this.refreshAccessTokenApi(appid, platform_entity.getRefreshToken());

			// set to entity
			platform_entity.setAccessToken(json.getString("authorizer_access_token"));
			platform_entity.setAccessTokenCreate(nowtime);
			platform_entity.setAccessTokenExpireIn(Long.parseLong(json.getString("expires_in")));

			// change refresh token if different and not null
			String refresh_token = json.getString("authorizer_refresh_token");
			if (StringUtil.isNotBlank(refresh_token)
					&& !StringUtil.equals(refresh_token, platform_entity.getRefreshToken())) {
				platform_entity.setRefreshToken(refresh_token);
				platform_entity.setRefreshTokenCreate(nowtime);
			}

			// save to db
			tempPlatform.saveById();
		}

		// last, if platform username or nickname is empty, get info from server
		if (StringUtil.isBlank(platform_entity.getUserName()) || StringUtil.isBlank(platform_entity.getNickName())) {
			try {
				// get info and save to db
				JSONObject json = this.getAuthorizerInfoApi(appid);

				JSONObject authorizer_info = json.getJSONObject("authorizer_info");
				platform_entity.setUserName(authorizer_info.getString("user_name"));
				platform_entity.setNickName(authorizer_info.getString("nick_name"));
				platform_entity.setHeadImg(authorizer_info.getString("head_img"));
				platform_entity.setAlias(authorizer_info.getString("alias"));
				platform_entity.setQrcodeUrl(authorizer_info.getString("qrcode_url"));

				JSONObject service_type_info = authorizer_info.getJSONObject("service_type_info");
				platform_entity.setServiceTypeInfo(service_type_info.getInteger("id"));

				JSONObject verify_type_info = authorizer_info.getJSONObject("verify_type_info");
				platform_entity.setVerifyTypeInfo(verify_type_info.getInteger("id"));

				platform_entity.setBusinessInfo(authorizer_info.getJSONObject("business_info").toString());

				// save to db
				tempPlatform.saveById();

			} catch (Exception ex) {
				// not main function, just catch all
				log.warn("getAuthorizerInfoApi failed", ex);
			}
		}

		// create model that is platform component
		PlatformComponentModel model = new PlatformComponentModel(this.proxy, appid, platform_entity.getAccessToken());

		return model;
	}

	/**
	 * get platform model from current component by given appid, maybe self platform or platform component
	 * 
	 * @param appid
	 * @return
	 * @throws CoreException
	 * @throws DataException
	 * @throws CommonException
	 */
	public PlatformModel getPlatformModel(String appid) throws CoreException, DataException, CommonException {
		return this.getPlatformModel(appid, null, null, null);
	}

	/**
	 * get platform by auth code when the platform do re-auth, can get appid by auth code, return platform
	 * 
	 * @param auth_code
	 * @param expires_in
	 * @return
	 * @throws CommonException
	 * @throws DataException
	 * @throws CoreException
	 */
	public PlatformModel getPlatformModelByAuthCode(String auth_code, String expires_in)
			throws CoreException, DataException, CommonException {

		// get app info by auth code
		JSONObject auth_info = this.getAuthorizationInfoApi(auth_code);

		// get root info
		JSONObject info = auth_info.getJSONObject("authorization_info");

		// get appid
		String authorizer_appid = info.getString("authorizer_appid");

		return this.getPlatformModel(authorizer_appid, auth_info, auth_code, expires_in);
	}

	/**
	 * mainly for background work, touch for all platform for refresh ( maybe no need refresh)
	 * 
	 * @throws CommonException
	 */
	public void touchAllPlatform() throws CommonException {
		PlatformEntity platform_entity = new PlatformEntity();

		// get all platform entity
		List<Long> rowids = this.proxy.getQueryService().selectRowidsByAll(platform_entity);

		if (rowids != null) {
			for (Long rowid : rowids) {
				if (this.proxy.getEntityService().trySelectEntityByRowid(platform_entity, rowid)) {
					// touch item and ignore any error
					try {
						this.getPlatformModel(platform_entity.getId());
					} catch (Exception ex) {
						log.warn("touchAllPlatform item failed, id={}, ex={}", platform_entity.getId(), ex);
					}
				}
			} // for
		}
	}

}
