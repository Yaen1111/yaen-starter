package org.yaen.starter.core.model.models.wechat;

import java.util.ArrayList;
import java.util.List;

import org.yaen.starter.common.dal.entities.wechat.MenuEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.models.wechat.objects.Button;
import org.yaen.starter.core.model.services.WechatService;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * the whole menu model, make up with buttons, up to 3 top-level button
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
public class MenuModel extends OneModel {

	@Getter
	private WechatService service;

	/** the menu list */
	@Getter
	@Setter
	private List<MenuEntity> menuList;

	/** the buttons */
	@Getter
	private List<Button> buttons = new ArrayList<Button>();

	/**
	 * get menu as json string
	 * 
	 * @return
	 */
	public String getJsonMenu() {
		// to json
		String json = JSONObject.toJSONString(this.buttons);

		// replace null, as we no not need it
		return json.replaceAll(",null", "");
	}

	/**
	 * construct new model with service
	 * 
	 * @param service
	 */
	public MenuModel(WechatService service) {
		super("1.0.0");

		this.service = service;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#check()
	 */
	@Override
	public void check() throws CoreException {
		if (this.menuList == null)
			throw new CoreException("model not loaded");
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#clear()
	 */
	@Override
	public void clear() {
		this.menuList = null;
		this.buttons.clear();
	}

	/**
	 * load menu by given group
	 * 
	 * @param groupName
	 * @throws CoreException
	 */
	public void load(String groupName) throws CoreException {
		this.service.loadMenu(this, groupName);
	}

	/**
	 * save menu to db
	 * 
	 * @throws CoreException
	 */
	public void save() throws CoreException {
		this.service.save(this);
	}

	/**
	 * push menu to server
	 * 
	 * @throws CoreException
	 */
	public void push() throws CoreException {
		this.check();
		this.service.pushMenu(this.getJsonMenu());
	}

}
