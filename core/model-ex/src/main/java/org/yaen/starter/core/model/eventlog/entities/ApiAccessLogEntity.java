package org.yaen.starter.core.model.eventlog.entities;

import java.util.Date;

import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.util.utils.DateUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * api access log, can be put in same entity, or separate to different entity for performance
 * 
 * @author Yaen 2016年7月13日上午12:28:43
 */
@Getter
@Setter
public class ApiAccessLogEntity extends OneEntity {
	private static final long serialVersionUID = 7999039275787803573L;

	/** the api name/controller name, used for multy-api log, or leave empty for single api */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String apiName;

	/** the log time */
	@OneData(DataType = DataTypes.DATETIME)
	private Date logTime;

	/** the client ip */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String clientIp;

	/** the request method, get/post/head */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String requestMethod;

	/** the request header info */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String requestHeader;

	/** the request param info */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String requestParam;

	/**
	 * @see org.yaen.starter.common.dal.entities.OneEntity#BeforeInsert()
	 */
	@Override
	public boolean BeforeInsert() {
		if (super.BeforeInsert()) {
			this.logTime = DateUtil.getNow();
			return true;
		}
		return false;
	}

	/**
	 * @see org.yaen.starter.core.model.entities.TwoEntity#BeforeUpdate()
	 */
	@Override
	public boolean BeforeUpdate() {
		return false;
	}

	/**
	 * @see org.yaen.starter.common.dal.entities.OneEntity#BeforeDelete()
	 */
	@Override
	public boolean BeforeDelete() {
		return false;
	}

}
