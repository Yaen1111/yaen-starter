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
@OneUniqueIndex({ "ID", })
public class PlatformEntity extends TwoEntity {
	private static final long serialVersionUID = 1176918958761154976L;

	/** the username of the platform */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String userName;

	/** the user-defined name, maybe null */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String alias;

	/** the nickname of the platform */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String nickName;

	/** the head img url */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String headImg;

	/** the service type info, see ServiceTypes */
	@OneData(DataType = DataTypes.INT)
	private Integer serviceTypeInfo;

	/** the verify type info, see VerifyTypes */
	@OneData(DataType = DataTypes.INT)
	private Integer verifyTypeInfo;

	/** the qrcode url */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String qrcodeUrl;

	/** the authorization code */
	@OneData(DataType = DataTypes.VARCHAR512)
	private String authorizationCode;

	/** the authorization code expired time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long authorizationCodeExpiredTime;

	/** the access token for component */
	@OneData(DataType = DataTypes.VARCHAR512)
	private String accessToken;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long accessTokenCreate;

	/** the access token expire in, typically 7200 */
	@OneData(DataType = DataTypes.BIGINT)
	private Long accessTokenExpireIn;

	/** the refresh token for component */
	@OneData(DataType = DataTypes.VARCHAR512)
	private String refreshToken;

	/** the create time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long refreshTokenCreate;

	/** the business info, may contain open_store,open_scan,open_pay,open_card,open_shake, and more, just json string */
	@OneData(DataType = DataTypes.VARCHAR512)
	private String businessInfo;

	/** the func info, current 1-15, comma separated */
	@OneData(DataType = DataTypes.VARCHAR512)
	private String funcInfo;

}
