package org.yaen.starter.core.model.wechat.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * the componet binded platform entity
 * <p>
 * id=platform appid
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "WX_COMPONENT_PLATFORM")
@OneUniqueIndex({ "ID" })
public class ComponentPlatformEntity extends TwoEntity {
	private static final long serialVersionUID = 1176918958761154976L;

	/** the app secret */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String secret;

	/** the token for certificate, but controller do not known what app is, so need multi controller or same token */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String token;

	/** the admin email address */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String adminEmail;

	/** the admin password, if applicable */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String adminPassword;

	/** the account id, like gh_xxxx or wx_xxxx */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String accountId;

	/** the app title, no special usage */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String title;

	/** the app description, no special usage */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String description;

	/** the shopid, for wechat-wifi only */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String shopId;

	/** the ssid, for wechat-wifi only */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String ssid;

	/**
	 * empty constructor
	 */
	public ComponentPlatformEntity() {
		super();
	}

	/**
	 * constructor with appid
	 * 
	 * @param appid
	 */
	public ComponentPlatformEntity(String appid) {
		super(appid);
	}

}
