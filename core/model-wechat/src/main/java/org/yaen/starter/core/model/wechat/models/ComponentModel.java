package org.yaen.starter.core.model.wechat.models;

import java.util.HashMap;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.ComponentEntity;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;

/**
 * the wechat component model, for 3rd-party open platform
 * <p>
 * the most important different is the method to get access_token
 * 
 * @author Yaen 2016年7月18日下午9:28:07
 */
public class ComponentModel extends TwoModel<ComponentEntity> {

	@Getter
	private String componentAppid;

	/**
	 * call api and check result
	 * 
	 * @param api
	 * @param param
	 * @return
	 * @throws CoreException
	 */
	protected JSONObject callAPI(String api, Map<String, Object> param) throws CoreException {
		final String ERROR_CODE = "errorcode";

		JSONObject json = null;
		try {
			json = this.proxy.getHttpClient().httpsPostAsJson(api, param);
		} catch (Exception ex) {
			throw new CoreException("call api failed", ex);
		}

		// check error code
		if (json.containsKey(ERROR_CODE)) {
			throw new CoreException("call api return with error code " + json.getLong(ERROR_CODE));
		}

		return json;
	}

	/**
	 * constructor
	 * 
	 * @param proxy
	 * @param sample
	 */
	public ComponentModel(ProxyService proxy, ComponentEntity sample) {
		super(proxy, sample);

		// get appid
		this.componentAppid = WechatPropertiesUtil.getComponentAppid();
	}

	/**
	 * get platform model from current component
	 * 
	 * @return
	 */
	public PlatformModel getPlatformModel() {
		return null;
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
	 * @throws DataNotExistsException
	 */
	public String getComponentAccessToken() throws CoreException, DataNotExistsException {
		final String API = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
		final String CACHE_KEY = "component_access_token";

		Long now = DateUtil.getNow().getTime();

		String componentAccessToken = "";

		// can be cached, check cache first
		{
			componentAccessToken = (String) this.proxy.getRedisClient().getObjectByKey(CACHE_KEY);
			if (StringUtil.isNotBlank(componentAccessToken)) {
				return componentAccessToken;
			}
		}

		// if no cache, load from db
		{
			this.loadById(this.componentAppid);
			componentAccessToken = this.entity.getComponentAccessToken();
			Long create = this.entity.getComponentAccessTokenCreate();
			Long expirein = this.entity.getComponentAccessTokenExpireIn();

			// db is ok and not out of date
			if (StringUtil.isNotBlank(componentAccessToken) && create + expirein / 2 > now) {
				this.proxy.getRedisClient().saveObjectByKey(CACHE_KEY, componentAccessToken,
						(int) (now - create - expirein / 2));
				return componentAccessToken;
			}
		}

		// db is out of date, call api to get new
		{
			// get ticket, whitch is send by wechat server every 10 minutes
			String component_verify_ticket = this.entity.getComponentVerifyTicket();

			// make param value
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("component_appid", this.componentAppid);
			param.put("component_appsecret", WechatPropertiesUtil.getComponentSecret());
			param.put("component_verify_ticket", component_verify_ticket);

			// call api
			JSONObject json = this.callAPI(API, param);

			// get result value
			componentAccessToken = json.getString("component_access_token");
			Long expires_in = json.getLong("expires_in");

			this.entity.setComponentAccessToken(componentAccessToken);
			this.entity.setComponentAccessTokenCreate(now);
			this.entity.setComponentAccessTokenExpireIn(expires_in);

			// save
			this.saveById();

			// set cache
			this.proxy.getRedisClient().saveObjectByKey(CACHE_KEY, componentAccessToken, expires_in.intValue());
		}

		// done
		return componentAccessToken;
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
	 * @throws DataNotExistsException
	 */
	public String getPreAuthCode(String componentAccessToken) throws CoreException, DataNotExistsException {
		final String API = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
		final String CACHE_KEY = "pre_auth_code";

		Long now = DateUtil.getNow().getTime();

		String preAuthCode = "";

		// can be cached, check cache first
		{
			preAuthCode = (String) this.proxy.getRedisClient().getObjectByKey(CACHE_KEY);
			if (StringUtil.isNotBlank(preAuthCode)) {
				return preAuthCode;
			}
		}

		// if no cache, load from db
		{
			this.loadById(this.componentAppid);
			preAuthCode = this.entity.getPreAuthCode();
			Long create = this.entity.getPreAuthCodeCreate();
			Long expirein = this.entity.getPreAuthCodeExpireIn();

			// db is ok and not out of date
			if (StringUtil.isNotBlank(preAuthCode) && create + expirein / 2 > now) {
				this.proxy.getRedisClient().saveObjectByKey(CACHE_KEY, preAuthCode,
						(int) (now - create - expirein / 2));
				return preAuthCode;
			}
		}

		// db is out of date, call api to get new
		{
			// make param value
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("component_appid", this.componentAppid);

			// call api
			JSONObject json = this.callAPI(API, param);

			// get result value
			preAuthCode = json.getString("pre_auth_code");
			Long expires_in = json.getLong("expires_in");

			this.entity.setPreAuthCode(preAuthCode);
			this.entity.setPreAuthCodeCreate(now);
			this.entity.setPreAuthCodeExpireIn(expires_in);

			// save
			this.saveById();

			// set cache
			this.proxy.getRedisClient().saveObjectByKey(CACHE_KEY, preAuthCode, expires_in.intValue());
		}

		// done
		return preAuthCode;
	}

}
