package org.yaen.starter.common.dal.entities.wechat;

import java.util.Date;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * the access token, one appid only has one
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "ZWX_ACCESS_TOKEN")
@OneUniqueIndex({ "ID", "APP_ID" })
public class AccessTokenEntity extends TwoEntity {
	private static final long serialVersionUID = 3421207085062757758L;

	/** the main wechat appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String appId;

	// 9Jqm3wJOE2tmkhNFrrRtqFCifCmaUXNBUBMM2DDLJ5RxW5LT6yV64lXcGS1AjBYdNpO2xL-aymeCNpDZR15UlIwqnDfBUHUPEqavLrz9u01g8sqoJFH76WbuohsJA6gvBLWhAHAMWH
	// TH6V3P5TQgAt8pt_LaOtCVnr1c9-a3XEnvWZa7HupaNk7hHANO1yimB4dTL1hJa0iZClpLHiTWjxN3TwqtRioU7bhA9xPuET_pV4bdpBMgEqcQAZbHL0xkDlAhDMjv79IJNfABAVWA
	/** the access token, need 512 */
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 512)
	private String accessToken;

	/** the access token create time */
	@OneData(DataType = DataTypes.DATETIME)
	private Date createTime;

	/** expire in time, typically 7200, in seconds */
	@OneData(DataType = DataTypes.INT)
	private Integer expireIn;

	/** the access token get time plus expire in to get expire time */
	@OneData(DataType = DataTypes.DATETIME)
	private Date expireTime;

	/**
	 * empty constructor
	 */
	public AccessTokenEntity() {
		super();
	}

	/**
	 * constructor with appid
	 * 
	 * @param appId
	 */
	public AccessTokenEntity(String appId) {
		this();

		this.appId = appId;
	}

}
