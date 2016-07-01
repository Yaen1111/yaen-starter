package org.yaen.starter.core.model.models.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaen.starter.common.dal.entities.wechat.MenuEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.objects.NameValue;
import org.yaen.starter.common.data.objects.NameValueT;
import org.yaen.starter.common.data.objects.QueryBuilder;
import org.yaen.starter.common.integration.clients.WechatClient;
import org.yaen.starter.common.integration.contexts.GeneralCacheManager;
import org.yaen.starter.common.util.utils.PropertiesUtil;
import org.yaen.starter.core.model.contexts.ServiceManager;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.models.wechat.enums.ButtonTypes;
import org.yaen.starter.core.model.models.wechat.objects.AccessToken;
import org.yaen.starter.core.model.models.wechat.objects.Button;
import org.yaen.starter.core.model.models.wechat.objects.ClickButton;
import org.yaen.starter.core.model.models.wechat.objects.ComplexButton;
import org.yaen.starter.core.model.models.wechat.objects.ViewButton;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * the whole menu model, make up with buttons, up to 3 top-level button
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
@Slf4j
public class MenuModel extends OneModel {
	/** top menu(level 1) max count is 3 */
	public static int WECHAT_MAX_TOP_MENU_COUNT = 3;

	/** access token key in cache */
	private static String ACCESS_TOKEN_KEY = "WECHAT_ACCESS_TOKEN";

	/** the menu list */
	private List<MenuEntity> menuList;

	/** wechat client */
	private WechatClient wechatClient = ServiceManager.getWechatClient();

	/** the buttons */
	@Getter
	private List<Button> buttons = new ArrayList<Button>();

	/**
	 * get access token from wechat, with cache
	 * 
	 * @return
	 * @throws CoreException
	 */
	private AccessToken getAccessToken() throws CoreException {

		// get token from cache
		AccessToken accessToken = (AccessToken) GeneralCacheManager.get(ACCESS_TOKEN_KEY);

		if (accessToken == null) {

			// get appid and secret
			String appid = PropertiesUtil.getProperty("wechat.appid");
			String secret = PropertiesUtil.getProperty("wechat.secret");

			// call client
			JSONObject jsonObject;
			try {
				jsonObject = wechatClient.getAccessToken(appid, secret);
			} catch (Exception ex) {
				throw new CoreException("get access token error", ex);
			}

			// check result
			if (jsonObject == null) {
				throw new CoreException("wechat get access token failed");
			}

			// set token
			accessToken = new AccessToken();
			accessToken.setToken(jsonObject.getString("access_token"));
			accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));

			GeneralCacheManager.set(ACCESS_TOKEN_KEY, accessToken, accessToken.getExpiresIn());

		}

		return accessToken;
	}

	/**
	 * get menu as json string
	 * 
	 * @return
	 */
	private String getJsonMenu() {
		// to json
		String json = JSONObject.toJSONString(this.buttons);

		// replace null, as we no not need it
		return json.replaceAll(",null", "");
	}

	/**
	 * empty constructor
	 */
	public MenuModel() {
		super("1.0.0");
	}

	/**
	 * load menu from database by group name and make the whole menu
	 * 
	 * @param groupName
	 * @throws CoreException
	 */
	public void load(String groupName) throws CoreException {

		// get all menu entity
		{
			MenuEntity entity = new MenuEntity();

			try {
				QueryBuilder qb = new QueryBuilder();
				qb.getWhereEquals().add(new NameValue("groupName", groupName));
				qb.getOrders().add(new NameValueT<Boolean>("orders", true));

				List<Long> rowids = queryService.selectRowidsByQuery(entity, qb);
				this.menuList = queryService.selectListByRowids(entity, rowids);
			} catch (CommonException ex) {
				throw new CoreException("get menu entity error", ex);
			}
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

		// clear old buttons
		this.buttons = new ArrayList<Button>();

		// load all top menu again, put top menu in order and re-make none-parent button
		for (MenuEntity entity : this.menuList) {
			if (entity.getLevel() == 1) {
				// check max size
				if (this.buttons.size() >= WECHAT_MAX_TOP_MENU_COUNT) {
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
	 * push menu to wechat server
	 * 
	 * @throws CoreException
	 */
	public void pushMenu() throws CoreException {

		// need access token
		AccessToken accessToken = this.getAccessToken();

		// get menu json
		String menu = this.getJsonMenu();

		// call client
		JSONObject jsonObject;
		try {
			jsonObject = wechatClient.createMenu(menu, accessToken.getToken());
		} catch (Exception ex) {
			throw new CoreException("wechat create menu failed", ex);
		}

		// check result
		if (jsonObject == null) {
			throw new CoreException("wechat create menu return null");
		} else {
			// check err code
			int errcode = jsonObject.getIntValue("errcode");
			if (errcode != 0) {
				throw new CoreException(
						"create menu failed, errcode=" + errcode + ", errmsg=" + jsonObject.getString("errmsg"));
			}
		}
	}

}
