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
 * wechat media entity, all media should upload to wechat server to get used
 * 
 * @author Yaen 2016年7月14日下午2:23:17
 */
@Getter
@Setter
@OneTable(TableName = "ZWX_MEDIA")
@OneUniqueIndex("ID")
@OneIndex({ "MEIDA_ID" })
public class MediaEntity extends TwoEntity {
	private static final long serialVersionUID = -6241996330057087553L;

	/** the media id, can be reused!!! */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String mediaId;

	/** the media type */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String mediaType;

	/** the create time, origin is int type */
	@OneData(DataType = DataTypes.BIGINT)
	private Long createTime;

	/** the media keep for 3 days, after will be expired */
	@OneData(DataType = DataTypes.BIGINT)
	private Long expireTime;

}
