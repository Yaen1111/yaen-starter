package org.yaen.starter.core.model.wechat.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * the component entity, mainly for verify tickets
 * <p>
 * id=component appid
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "WX_COMPONENT")
@OneUniqueIndex({ "ID" })
public class ComponentEntity extends TwoEntity {
	private static final long serialVersionUID = -4510666965270467874L;

	// ticket@@@7uUrwcEAJnyKOzNby9vFFaVHPUoi1BEq_EspgoVYYcuHG-cpsMuplS5SJu-xSjBdQ93ih4Mb5hqZFdIT6WRTkg
	/** the component verify ticket, changes every 10 minutes */
	@OneData(DataType = DataTypes.VARCHAR512)
	private String componentVerifyTicket;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long componentVerifyTicketCreate;

	/** the component access token, expires in 7200 */
	@OneData(DataType = DataTypes.VARCHAR512)
	private String componentAccessToken;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long componentAccessTokenCreate;

	/** the expire in, typically 7200 */
	@OneData(DataType = DataTypes.BIGINT)
	private Long componentAccessTokenExpireIn;

	/** the pre auth code, expires in 600 */
	@OneData(DataType = DataTypes.VARCHAR512)
	private String preAuthCode;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long preAuthCodeCreate;

	/** the expire in, typically 7200 */
	@OneData(DataType = DataTypes.BIGINT)
	private Long preAuthCodeExpireIn;

}
