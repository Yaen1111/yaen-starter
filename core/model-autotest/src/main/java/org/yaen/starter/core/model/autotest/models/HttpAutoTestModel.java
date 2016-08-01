package org.yaen.starter.core.model.autotest.models;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.autotest.entities.AutoTestCaseEntity;
import org.yaen.starter.core.model.autotest.enums.TestTypes;
import org.yaen.starter.core.model.services.ProxyService;

/**
 * http auto test, include http/https get/post
 * 
 * @author Yaen 2016年8月1日下午2:54:36
 */
public class HttpAutoTestModel extends BaseAutoTestModel {

	/**
	 * empty constructor
	 * 
	 * @param proxy
	 */
	public HttpAutoTestModel(ProxyService proxy) {
		super(proxy);
	}

	/**
	 * @see org.yaen.starter.core.model.autotest.models.BaseAutoTestModel#getTestResponse()
	 */
	@Override
	protected String getTestResponse() throws CoreException {
		AutoTestCaseEntity testcase = this.getEntity();

		String testtype = testcase.getTestType();

		AssertUtil.notBlank(testtype);

		// the response
		String response = "";

		try {
			// select test types
			switch (testtype) {
			case TestTypes.TEST_HTTP_GET:
				response = this.proxy.getHttpClient().httpGet(testcase.getApi());
				break;
			case TestTypes.TEST_HTTP_POST:
				response = this.proxy.getHttpClient().httpPost(testcase.getApi(), testcase.getParam());
				break;
			case TestTypes.TEST_HTTPS_GET:
				response = this.proxy.getHttpClient().httpsGet(testcase.getApi());
				break;
			case TestTypes.TEST_HTTPS_POST:
				response = this.proxy.getHttpClient().httpsPost(testcase.getApi(), testcase.getParam());
				break;
			default:
				throw new CoreException("test type not supported, type=" + testtype);
			}

		} catch (IOException ex) {
			throw new CoreException("http client io error", ex);
		} catch (GeneralSecurityException ex) {
			throw new CoreException("http client security error", ex);
		}

		return response;
	}

}
