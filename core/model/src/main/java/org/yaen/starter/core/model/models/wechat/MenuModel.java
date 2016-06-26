package org.yaen.starter.core.model.models.wechat;

import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.pojos.wechat.Button;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * the whole menu model, make up with buttons, up to 3 top-level button
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
public class MenuModel extends OneModel {

	/** the buttons */
	@Getter
	@Setter
	private Button[] buttons;

	/**
	 * to json menu
	 * 
	 * @return
	 */
	public String toJSON() {
		// to json
		String json = JSONObject.toJSONString(this);

		// replace null, as we no not need it
		return json.replaceAll(",null", "");
	}

}
