package org.yaen.starter.core.model.wechat.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * the component binded platform entity
 * <p>
 * id=platform appid
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "WX_PLATFORM")
@OneUniqueIndex({ "ID" })
public class PlatformEntity extends TwoEntity {
	private static final long serialVersionUID = 1176918958761154976L;

	/** the authorization code */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String authorizationCode;

	/** the authorization code expired time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long authorizationCodeExpiredTime;

	/** the access token for component */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String accessToken;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long accessTokenCreate;

	/** the access token expire in, typically 7200 */
	@OneData(DataType = DataTypes.BIGINT)
	private Long accessTokenExpireIn;

	/** the refresh token for component */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String refreshToken;

	/** the create time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long refreshTokenCreate;

	/** the func scope category, current 1-15, comma separated */
	@OneData(DataType = DataTypes.VARCHAR256)
	private String funcscopeCategory;

}
