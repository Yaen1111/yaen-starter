package org.yaen.starter.core.model.models.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaen.starter.common.dal.entities.wechat.MenuEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.contexts.ServiceLoader;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.models.wechat.enums.ButtonTypes;
import org.yaen.starter.core.model.models.wechat.objects.AccessToken;
import org.yaen.starter.core.model.models.wechat.objects.Button;
import org.yaen.starter.core.model.models.wechat.objects.ClickButton;
import org.yaen.starter.core.model.models.wechat.objects.ComplexButton;
import org.yaen.starter.core.model.models.wechat.objects.ViewButton;
import org.yaen.starter.core.model.services.WechatService;

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

	/** the menu list */
	private List<MenuEntity> menuList;

	/** the service */
	private WechatService service = ServiceLoader.getWechatService();

	/** the buttons */
	@Getter
	private List<Button> buttons = new ArrayList<Button>();

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
		// get entity
		this.menuList = service.getMenuEntityList(groupName);

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

	}

	/**
	 * push menu to wechat server
	 * 
	 * @throws CoreException
	 */
	public void pushMenu() throws CoreException {

		// need access token
		AccessToken accessToken = service.getAccessToken();

		// call service to create menu
		service.createMenu(this.toJSONString(), accessToken);
	}

	/**
	 * to json string
	 * 
	 * @return
	 */
	public String toJSONString() {
		// to json
		String json = JSONObject.toJSONString(this.buttons);

		// replace null, as we no not need it
		return json.replaceAll(",null", "");
	}

}
