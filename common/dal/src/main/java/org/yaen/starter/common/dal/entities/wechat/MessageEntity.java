package org.yaen.starter.common.dal.entities.wechat;

import java.math.BigDecimal;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * wechat message entity
 * <p>
 * group by msg type, if is event, then group by event type
 * <p>
 * all message is in xml format, only in passive mode, from user to server, and server response to user
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */
@Getter
@Setter
@OneTable(TableName = "ZWX_MESSAGE")
@OneUniqueIndex("ID")
@OneIndex({ "FROM_USER_NAME", "TO_USER_NAME", "MSG_TYPE,EVENT" })
public class MessageEntity extends TwoEntity {
	private static final long serialVersionUID = -3762180033509714839L;

	/** to user name, when receive is the appid, when send is openid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String toUserName;

	/** from user name, when receive is the openid, when send is appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String fromUserName;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long createTime;

	/** the message type */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String msgType;

	/** the event type */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String event;

	/** the event key, bind to event type, can be any type including url */
	@OneData(DataType = DataTypes.VARCHAR1000)
	private String eventKey;

	/** the ticket for qrcode */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String ticket;

	/** for image/voice/video/etc, the media id, used to download media */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String mediaId;

	/** for video/short video/music, the thumb media id, used to download media */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String thumbMediaId;

	/** for text, the message id, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long msgId;

	/** for text, the message content */
	@OneData(DataType = DataTypes.VARCHAR1000)
	private String content;

	/** for image, the picture url */
	@OneData(DataType = DataTypes.VARCHAR1000)
	private String picUrl;

	/** for music, the music url */
	@OneData(DataType = DataTypes.VARCHAR1000)
	private String musicURL;

	/** for music, the high-quality music url, used in wifi */
	@OneData(DataType = DataTypes.VARCHAR1000)
	private String hqMusicUrl;

	/** for news, the count of article, max 10, the first is large image */
	@OneData(DataType = DataTypes.INT)
	private Integer articleCount;

	/** for news, the content of article, max 10, with list, here just use json */
	@OneData(DataType = DataTypes.VARCHAR1000)
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

	/** for link, the title of the link */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String title;

	/** for link, the description of the link */
	@OneData(DataType = DataTypes.VARCHAR250)
	private String description;

	/** for link, the url of the link */
	@OneData(DataType = DataTypes.VARCHAR1000)
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

}
