package org.yaen.starter.core.model.wechat.entities;

import java.util.Date;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * the wechat user, mainly for wechat appid and openid
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "WX_PLATFORM_USER")
@OneUniqueIndex({ "ID", "OPEN_ID,APP_ID" })
public class PlatformUserEntity extends TwoEntity {
	private static final long serialVersionUID = -4660940072642230728L;

	/** the main wechat openid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String openId;

	/** the main wechat appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String appId;

	/** the last bind terminal userid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String lastTerminalUserId;

	/** last active time */
	@OneData(DataType = DataTypes.DATETIME)
	private Date lastActiveTime;

	/** is subscribed, 1 for subscribed */
	@OneData(DataType = DataTypes.INT)
	private Integer subscribe;

	/** subscribe time, is the last subscribe time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long subscribeTime;

	/** unsubscribe time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long unsubscribeTime;

	/** the nickname, used for identify between appid */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String nickname;

	/** the sex, 1=mail, 2=femail, 0=unknown */
	@OneData(DataType = DataTypes.INT)
	private Integer sex;

	/** the city */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String city;

	/** the country */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String country;

	/** the province */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String province;

	/** the language, zh_CN for S Chinese */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String language;

	/** the headimgurl */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String headimgurl;

	/** the unionId, should bind to developer platform, null in most case */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String unionId;

	/** the remark, set by platform */
	@OneData(DataType = DataTypes.VARCHAR256)
	private String remark;

	/** the groupId, for old use */
	@OneData(DataType = DataTypes.INT)
	private Integer groupId;

	/** the tagIdList, for tags, like "[128,2]" */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String tagIdList;

}
