package org.yaen.starter.core.model.wechat.utils;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.ParseUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * wechat errcode util, used to check errcode and format exception
 * 
 * @author Yaen 2016年7月14日下午5:52:30
 */
public class WechatJsonUtil {

	/**
	 * check json for err code
	 * 
	 * @param json
	 * @throws CoreException
	 */
	public static void checkErrCode(JSONObject json) throws CoreException {

		// check error code, should exists and not 0
		if (json.containsKey("errcode")) {
			Integer errcode = ParseUtil.tryParseInt(json.getString("errcode"), 0);
			if (errcode != 0) {
				throw new CoreException("call api return with error, result=" + json.toJSONString());
			}
		}

	}
}
