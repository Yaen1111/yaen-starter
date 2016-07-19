package org.yaen.starter.core.model.wechat.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * wechat component message entity
 * <p>
 * from wechat server to component
 * <p>
 * use infoType as type key
 * 
 * @author Yaen 2016年7月18日下午10:12:05
 */
@Getter
@Setter
@OneTable(TableName = "WX_COMPONENT_MESSAGE")
@OneUniqueIndex("ID")
public class ComponentMessageEntity extends TwoEntity {
	private static final long serialVersionUID = 6428714660926496899L;

	/** component appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String appid;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long createTime;

	/** the info type */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String infoType;

	/** the authorizer appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String authorizerAppid;

	/** the authorization code */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String authorizationCode;

	/** the authorization code expired time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long authorizationCodeExpiredTime;

	/** the component verify ticket */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String componentVerifyTicket;

}
