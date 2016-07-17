package org.yaen.starter.core.model.entities.wechat;

import java.util.Date;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * the app, in case of manager many app
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "ZWX_APP")
@OneUniqueIndex({ "ID", "APP_ID" })
public class AppEntity extends TwoEntity {
	private static final long serialVersionUID = 1176918958761154976L;

	/** the main wechat appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String appId;

	/** the app secret */
	@OneData(DataType = DataTypes.VARCHAR32)
	private Date secret;

	/** the token for certificate, but controller do not known what app is, so need multi controller or same token */
	@OneData(DataType = DataTypes.VARCHAR32)
	private Date token;

	/** the admin email address */
	@OneData(DataType = DataTypes.VARCHAR64)
	private Date adminEmail;

	/** the admin password, if applicable */
	@OneData(DataType = DataTypes.VARCHAR64)
	private Date adminPassword;

	/** the account id, like gh_xxxx or wx_xxxx */
	@OneData(DataType = DataTypes.VARCHAR32)
	private Date accountId;

	/** the app title, no special usage */
	@OneData(DataType = DataTypes.VARCHAR32)
	private Date title;

	/** the app description, no special usage */
	@OneData(DataType = DataTypes.VARCHAR32)
	private Date description;

	/** the shopid, for wechat-wifi only */
	@OneData(DataType = DataTypes.VARCHAR32)
	private Date shopId;

	/** the ssid, for wechat-wifi only */
	@OneData(DataType = DataTypes.VARCHAR32)
	private Date ssid;

	/** the bssid, for wechat-wifi only, currently no use */
	@OneData(DataType = DataTypes.VARCHAR32)
	private Date bssid;

	/**
	 * empty constructor
	 */
	public AppEntity() {
		super();
	}

	/**
	 * constructor with appid
	 * 
	 * @param appId
	 */
	public AppEntity(String appId) {
		this();

		this.appId = appId;
	}

}
