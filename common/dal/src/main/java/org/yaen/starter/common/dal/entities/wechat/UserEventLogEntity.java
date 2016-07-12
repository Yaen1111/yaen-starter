package org.yaen.starter.common.dal.entities.wechat;

import java.util.Date;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * the user event/activity info, mainly for wechat messages
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "ZWX_USER_EVENTLOG")
@OneIndex("OPEN_ID,APP_ID")
public class UserEventLogEntity extends TwoEntity {
	private static final long serialVersionUID = -8593679209601430679L;

	/** the main wechat openid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String openId;

	/** the main wechat appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String appId;

	/** the event type */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String eventType;

	/** the event time */
	@OneData(DataType = DataTypes.DATETIME)
	private Date eventTime;

}
