package org.yaen.starter.core.model.wechat.models;

import java.util.HashMap;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.NoDataAffectedException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.ComponentEntity;
import org.yaen.starter.core.model.wechat.entities.PlatformEntity;
import org.yaen.starter.core.model.wechat.utils.WechatJsonUtil;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;

/**
 * the wechat component model, for 3rd-party open platform
 * <p>
 * the most important difference is the method to get access_token
 * 
 * @author Yaen 2016年7月18日下午9:28:07
 */
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
			json = this.proxy.getHttpClient().httpsPostAsJson(api, param);
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

		Long now = DateUtil.getNow().getTime();

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
			if (StringUtil.isNotBlank(component_access_token) && create + expirein / 2 > now) {
				// set cache
				this.writeCache(CACHE_KEY, component_access_token, (int) (now - create - expirein / 2));
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
			this.getEntity().setComponentAccessTokenCreate(now);
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

		Long now = DateUtil.getNow().getTime();

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
			if (StringUtil.isNotBlank(pre_auth_code) && create + expirein / 2 > now) {
				// set cache
				this.writeCache(CACHE_KEY, pre_auth_code, (int) (now - create - expirein / 2));
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
			this.getEntity().setPreAuthCodeCreate(now);
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
		final String API = "https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";

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
	 * get platform model from current component by given appid, maybe self platform or platform component
	 * 
	 * @param appid
	 * @return
	 * @throws CoreException
	 * @throws CommonException
	 * @throws DataException
	 */
	public PlatformModel getPlatformModel(String appid) throws CoreException, DataException, CommonException {
		AssertUtil.notBlank(appid);

		// if the appid is self, just return new platform
		if (StringUtil.equals(appid, WechatPropertiesUtil.getAppid())) {
			return new PlatformModel(this.proxy);
		}

		Long now = DateUtil.getNow().getTime();

		// find platform from db
		PlatformEntity platform = null;
		try {
			platform = this.proxy.getQueryService().selectOneById(new PlatformEntity(), appid);
		} catch (CommonException ex) {
			throw new CoreException("get platform entity failed", ex);
		}

		// need refresh token, if null, call api to get one
		if (StringUtil.isBlank(platform.getRefreshToken())) {
			// get refresh token, need auth code
			if (StringUtil.isBlank(platform.getAuthorizationCode())
					|| platform.getAuthorizationCodeExpiredTime() <= now) {
				throw new CoreException(
						"the platform has no refresh token nor authorization code(or expired), must be re-authorized. appid="
								+ appid);
			}

			// call api to get auth code
			JSONObject json = this.getAuthorizationInfoApi(platform.getAuthorizationCode());

			// check appid
			String authorizer_appid = json.getString("authorizer_appid");
			if (!StringUtil.equals(authorizer_appid, appid)) {
				throw new CoreException(
						"the auth code of appid is not same with the given appid, maybe authcode assign error, please check and re-authorize. "
								+ "authorized appid=" + authorizer_appid + ", appid=" + appid);
			}

			// set to entity
			platform.setAccessToken(json.getString("authorizer_access_token"));
			platform.setAccessTokenCreate(now);
			platform.setAccessTokenExpireIn(Long.parseLong(json.getString("expires_in")));
			platform.setRefreshToken(json.getString("authorizer_refresh_token"));
			platform.setRefreshTokenCreate(now);

			// make func scope list
			// JSONArray arr = json.getJSONArray("func_info");
			// for(int i = 0; i<arr.size() ;i++){
			// arr.getJSONObject(0);
			// }

			// save to db
			try {
				this.proxy.getEntityService().updateEntityByRowid(platform);
			} catch (NoDataAffectedException ex) {
				throw new CoreException("save platform entity failed", ex);
			} catch (CommonException ex) {
				throw new CoreException("save platform entity failed", ex);
			}
		}

		// need to get access token, if null, call api to get new one, if expired, call api to refresh
		if (StringUtil.isBlank(platform.getAccessToken())
				|| platform.getAccessTokenCreate() + platform.getAccessTokenExpireIn() / 2 <= now) {

			// need refresh token
			if (StringUtil.isBlank(platform.getRefreshToken())) {
				throw new CoreException(
						"the platform has access token nor refresh token(or expired), must be re-authorized. appid="
								+ appid);
			}

			// call api to refresh token
			JSONObject json = this.refreshAccessTokenApi(appid, platform.getRefreshToken());

			// set to entity
			platform.setAccessToken(json.getString("authorizer_access_token"));
			platform.setAccessTokenCreate(now);
			platform.setAccessTokenExpireIn(Long.parseLong(json.getString("expires_in")));

			// change refresh token if different
			String refresh_token = json.getString("authorizer_refresh_token");
			if (!StringUtil.equals(refresh_token, platform.getRefreshToken())) {
				platform.setRefreshToken(json.getString("authorizer_refresh_token"));
				platform.setRefreshTokenCreate(now);
			}

			// save to db
			try {
				this.proxy.getEntityService().updateEntityByRowid(platform);
			} catch (NoDataAffectedException ex) {
				throw new CoreException("save platform entity failed", ex);
			} catch (CommonException ex) {
				throw new CoreException("save platform entity failed", ex);
			}
		}

		// create model that is platform component
		PlatformComponentModel model = new PlatformComponentModel(this.proxy, appid, platform.getAccessToken());

		return model;
	}

}
