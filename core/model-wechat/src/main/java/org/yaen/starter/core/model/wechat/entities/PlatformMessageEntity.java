package org.yaen.starter.core.model.wechat.entities;

import java.math.BigDecimal;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * wechat platform message entity
 * <p>
 * all message from user or wechat system, to server, and server can response to user
 * <p>
 * group by msg type, if is event, then group by event type
 * <p>
 * for none-event messages, msgId should be unique(in one appid)
 * <p>
 * for event messages, FromUserName + CreateTime should be unique(in one appid)
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */

@Getter
@Setter
@OneTable(TableName = "WX_PLATFORM_MESSAGE")
@OneUniqueIndex("ID")
@OneIndex({ "FROM_USER_NAME", "TO_USER_NAME", "MSG_TYPE,EVENT", "MSG_ID" })
public class PlatformMessageEntity extends TwoEntity {
	private static final long serialVersionUID = -3762180033509714839L;

	/** to user name, when receive is the appid, when send is openid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String toUserName;

	/** from user name, when receive is the openid (or system for some verify event), when send is appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String fromUserName;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long createTime;

	/** the message type */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String msgType;

	/** for none-event, the message id, should be unique within one appid, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long msgId;

	/** the event type */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String event;

	/** the event key, bind to event type, can be any type including url */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String eventKey;

	/** the ticket for qrcode */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String ticket;

	/** for link, the title of the link */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String title;

	/** for link, the description of the link */
	@OneData(DataType = DataTypes.VARCHAR256)
	private String description;

	/** for text, the message content */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String content;

	/** for image/voice/video/etc, the media id, used to download media */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String mediaId;

	/** for video/short video/music, the thumb media id, used to download media */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String thumbMediaId;

	/** for image, the picture url */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String picUrl;

	/** for music, the music url */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String musicURL;

	/** for music, the high-quality music url, used in wifi */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String hqMusicUrl;

	/** for news, the count of article, max 10, the first is large image */
	@OneData(DataType = DataTypes.INT)
	private Integer articleCount;

	/** for news, the content of article, max 10, with list, here just use json */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String articles;

	/** for voice, the format of voice, like amr，speex */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String format;

	/** for voice, the recognition of voice, is text */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String recognition;

	/** for location, the location x like 23.134521 */
	@OneData(DataType = DataTypes.DECIMAL, DataSize = 10, ScaleSize = 6)
	private BigDecimal locationX;

	/** for location, the location y like 113.358803 */
	@OneData(DataType = DataTypes.DECIMAL, DataSize = 10, ScaleSize = 6)
	private BigDecimal locationY;

	/** for location, the scale of map, like 20 */
	@OneData(DataType = DataTypes.INT)
	private Integer scale;

	/** for location, the label of location, text */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String label;

	/** for event-LOCATION, the latitude( location y) like 113.358803 */
	@OneData(DataType = DataTypes.DECIMAL, DataSize = 10, ScaleSize = 6)
	private BigDecimal latitude;

	/** for event-LOCATION, the longitude( location x) like 23.134521 */
	@OneData(DataType = DataTypes.DECIMAL, DataSize = 10, ScaleSize = 6)
	private BigDecimal longitude;

	/** for event-LOCATION, the precision of location like 119.385040 */
	@OneData(DataType = DataTypes.DECIMAL, DataSize = 10, ScaleSize = 6)
	private BigDecimal precision;

	/** for link, the url of the link */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String url;

	/** for event-wifi connected, connect time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long connectTime;

	/** for event-wifi connected, reserved */
	@OneData(DataType = DataTypes.BIGINT)
	private Long expireTime;

	/** for event-wifi connected, reserved */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String vendorId;

	/** for event-wifi connected, the shop id */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String shopId;

	/** for event-wifi connected, the terminal mac, as bssid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String deviceNo;

	/** for event-xxx_verify_success, the verify expired time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long expiredTime;

	/** for event-xxx_verify_fail, fail time */
	@OneData(DataType = DataTypes.BIGINT)
	private Long failTime;

	/** for event-xxx_verify_fail, fail reason */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String failReason;

	/** for event-poi, the shop owner id */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String uniqId;

	/** for event-poi, the shop id */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String poiId;

	/** for event-poi, succ for ok, fail for error */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String result;

	/** for event-poi, the check result message */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String msg;

}
