package org.yaen.starter.core.model.entities.wechat;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * menu entity for wechat
 * 
 * @author Yaen 2016年6月19日下午8:02:42
 */
@Getter
@Setter
@OneTable(TableName = "ZWX_MENU")
@OneUniqueIndex({ "ID", "APP_ID" })
public class MenuEntity extends TwoEntity {
	private static final long serialVersionUID = -6445208030709771936L;

	/** menu appid */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String appId;

	/** menu level, currently 1 or 2 */
	@OneData(DataType = DataTypes.INT)
	private Integer level;

	/** menu order, follow the level */
	@OneData(DataType = DataTypes.INT)
	private int orders;

	/** menu type */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String type;

	/** parent menu id */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String parentId;

	/** menu title */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String title;

	/** menu key */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String key;

	/** menu url, will jump to that url */
	@OneData(DataType = DataTypes.VARCHAR250)
	private String url;

	/** description */
	@OneData(DataType = DataTypes.VARCHAR250)
	private String description;

}
