package org.yaen.starter.core.model.models.wechat;

import java.util.ArrayList;
import java.util.List;

import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.pojos.wechat.Button;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;

/**
 * the whole menu model, make up with buttons, up to 3 top-level button
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
public class MenuModel extends OneModel {
	/** top menu(level 1) max count is 3 */
	public static int WECHAT_MAX_TOP_MENU_COUNT = 3;

	/** the buttons */
	@Getter
	private List<Button> buttons = new ArrayList<Button>();

	/**
	 * constructor
	 */
	public MenuModel() {
		super();
	}

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
