package org.yaen.starter.core.model.wechat.enums;

/**
 * verify types
 * 
 * @author Yaen 2016年7月27日下午9:28:20
 */
public final class VerifyTypes {

	/** 未认证 */
	public static final Integer NOT_VERIFIED = -1;

	/** 微信认证 */
	public static final Integer VERIFIED_BY_WECHAT = 0;

	/** 新浪微博认证 */
	public static final Integer VERIFIED_BY_WEIBO = 1;

	/** 腾讯微博认证 */
	public static final Integer VERIFIED_BY_TENCENT = 2;

	/** 已资质认证通过但还未通过名称认证 */
	public static final Integer PARTIAL = 3;

	/** 已资质认证通过、还未通过名称认证，但通过了新浪微博认证 */
	public static final Integer PARTIAL_WITH_WEIBO = 4;

	/** 已资质认证通过、还未通过名称认证，但通过了腾讯微博认证 */
	public static final Integer PARTIAL_WITH_TENCENT = 5;

}
