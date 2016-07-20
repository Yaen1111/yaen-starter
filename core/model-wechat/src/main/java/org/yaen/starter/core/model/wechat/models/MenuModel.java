package org.yaen.starter.core.model.wechat.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataOperationCancelledException;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.MenuEntity;
import org.yaen.starter.core.model.wechat.enums.ButtonTypes;
import org.yaen.starter.core.model.wechat.objects.Button;
import org.yaen.starter.core.model.wechat.objects.ClickButton;
import org.yaen.starter.core.model.wechat.objects.ComplexButton;
import org.yaen.starter.core.model.wechat.objects.ViewButton;
import org.yaen.starter.core.model.wechat.services.WechatService;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * the whole menu model, make up with buttons, up to 3 top-level button and with 1-5 sub button each
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
@Slf4j
public class MenuModel extends OneModel {
	/** top menu(level 1) max count is 3 */
	public static int MAX_TOP_MENU_COUNT = 3;

	/** the proxy */
	@Getter
	private ProxyService proxy;

	@Getter
	private WechatService service;

	/** the appid */
	@Getter
	@Setter
	private String appId;

	/** the menu list */
	@Getter
	@Setter
	private List<MenuEntity> menuList;

	/** the buttons */
	@Getter
	private List<Button> buttons = new ArrayList<Button>();

	/**
	 * constructor
	 * 
	 * @param proxy
	 * @param service
	 */
	public MenuModel(ProxyService proxy, WechatService service) {
		super();

		this.proxy = proxy;

		this.service = service;
	}

	/**
	 * get menu as json string
	 * 
	 * @return
	 */
	public String getJsonMenu() {
		// to json
		String json = JSONObject.toJSONString(this.buttons);

		// replace null, as we do not need it
		return json.replaceAll(",null", "");
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
	 * load menu by appId
	 * 
	 * @param appId
	 * @throws CoreException
	 * @throws DataException
	 */
	public void loadByAppId(String appId) throws CoreException, DataException {
		this.appId = appId;

		// load default if not given
		if (StringUtil.isBlank(this.appId)) {
			this.appId = WechatPropertiesUtil.getAppid();
		}

		// get menu entity list
		try {
			MenuEntity menu = new MenuEntity();
			menu.setAppId(appId);

			List<Long> rowids = this.proxy.getQueryService().selectRowidsByField(menu, "appId");
			this.menuList = this.proxy.getQueryService().selectListByRowids(menu, rowids);
		} catch (CommonException ex) {
			throw new CoreException("get menu entity error", ex);
		}

		// the object has no relation, so make it in temp
		Map<String, ComplexButton> top = new HashMap<String, ComplexButton>();

		// load all top menu
		for (MenuEntity entity : this.menuList) {
			if (entity.getLevel() == 1) {
				// top level, make complex button
				ComplexButton btn = new ComplexButton();
				btn.setName(entity.getTitle());
				top.put(entity.getId(), btn);
			}
		}

		// load all 2nd menu
		for (MenuEntity entity : this.menuList) {
			if (entity.getLevel() == 2) {
				// parent must exists
				if (top.containsKey(entity.getParentId())) {
					switch (entity.getType()) {
					case ButtonTypes.CLICK: {
						ClickButton btn = new ClickButton();
						btn.setType(entity.getType());
						btn.setName(entity.getTitle());
						btn.setKey(entity.getKey());
						top.get(entity.getParentId()).getSub_button().add(btn);
					}
						break;
					case ButtonTypes.VIEW: {
						ViewButton btn = new ViewButton();
						btn.setType(entity.getType());
						btn.setName(entity.getTitle());
						btn.setUrl(entity.getUrl());
						top.get(entity.getParentId()).getSub_button().add(btn);
					}
						break;
					default:
						// ignore
						log.info("unknown menu type, id={}, type={}", entity.getId(), entity.getType());
						break;
					}
				} else {
					log.info("menu parent not exists, id={}, parentid={}", entity.getId(), entity.getParentId());
				}
			}
		}

		// load all top menu again, put top menu in order and re-make none-parent button
		for (MenuEntity entity : this.menuList) {
			if (entity.getLevel() == 1) {
				// check max size
				if (this.buttons.size() >= MAX_TOP_MENU_COUNT) {
					log.info("menu max size exceed, rest button will be ignored");
					break;
				}

				// get btn and check child count
				ComplexButton topbtn = top.get(entity.getId());

				if (topbtn.getSub_button().size() > 0) {
					// has sub button
					this.buttons.add(topbtn);
				} else {
					// no sub button, is normal button
					switch (entity.getType()) {
					case ButtonTypes.CLICK: {
						ClickButton btn = new ClickButton();
						btn.setType(entity.getType());
						btn.setName(entity.getTitle());
						btn.setKey(entity.getKey());
						this.buttons.add(btn);
					}
						break;
					case ButtonTypes.VIEW: {
						ViewButton btn = new ViewButton();
						btn.setType(entity.getType());
						btn.setName(entity.getTitle());
						btn.setUrl(entity.getUrl());
						this.buttons.add(btn);
					}
						break;
					default:
						// ignore
						log.info("unknown menu type, id={}, type={}", entity.getId(), entity.getType());
						break;
					}
				}
			}
		} // for

		// all done
	}

	/**
	 * save menu to db
	 * 
	 * @throws CoreException
	 */
	public void save() throws CoreException {
		// TODO
	}

	/**
	 * pull menu from server
	 * 
	 * @param appId
	 * @throws CoreException
	 */
	public void pull(String appId) throws CoreException {

		this.appId = appId;

		// load default if not given
		if (StringUtil.isBlank(this.appId)) {
			this.appId = WechatPropertiesUtil.getAppid();
		}

		// call service
		// TODO
	}

	/**
	 * push menu to server
	 * 
	 * @throws CoreException
	 */
	public void push() throws CoreException {
		this.check();

		// call service
		this.service.pushMenu(this.getJsonMenu(), this.appId);
	}

}
