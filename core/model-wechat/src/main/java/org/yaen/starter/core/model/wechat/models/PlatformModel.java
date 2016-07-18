package org.yaen.starter.core.model.wechat.models;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.OneModel;

import lombok.Getter;

/**
 * the wechat official account platform model
 * <p>
 * only one, deals with most platform operations, only need access_token(both for platform or platform-comp)
 * 
 * @author Yaen 2016年7月18日下午9:28:01
 */
public class PlatformModel extends OneModel {

	/** the current access token */
	@Getter
	private String accessToken;

	/**
	 * constructor
	 */
	public PlatformModel(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#check()
	 */
	@Override
	public void check() throws CoreException {
		if (StringUtil.isBlank(this.accessToken))
			throw new CoreException("accessToken is empty");
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#clear()
	 */
	@Override
	public void clear() {
		this.accessToken = "";
	}

}
