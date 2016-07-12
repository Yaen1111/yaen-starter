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
 * the wechat user, mainly for wechat appid and openid
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "ZWX_USER")
@OneUniqueIndex({ "ID", "OPEN_ID,APP_ID" })
public class UserEntity extends TwoEntity {
	private static final long serialVersionUID = -4660940072642230728L;

	/** the main wechat openid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String openId;

	/** the main wechat appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String appId;

	/** the nickname, used for identify between appid */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String nickName;

	/** subscribe time */
	@OneData(DataType = DataTypes.DATETIME)
	private Date subscribeTime;

	/**
	 * empty constructor
	 */
	public UserEntity() {
		super();
	}

	/**
	 * constructor with appid and openid
	 * 
	 * @param openId
	 * @param appId
	 */
	public UserEntity(String openId, String appId) {
		this();

		this.openId = openId;
		this.appId = appId;
	}

}
