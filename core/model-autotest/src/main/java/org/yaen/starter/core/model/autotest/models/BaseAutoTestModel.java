package org.yaen.starter.core.model.autotest.models;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.TestException;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.autotest.entities.AutoTestCaseEntity;
import org.yaen.starter.core.model.autotest.entities.AutoTestResultEntity;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;

/**
 * base auto test model, used to set request and check response, the response is converted to string
 * <p>
 * step 1: set request and demanded response
 * <p>
 * step 2: override equals method if need
 * <p>
 * step 3: implement run test method, return ok or error
 * 
 * @author Yaen 2016年8月1日下午2:14:36
 */
public abstract class BaseAutoTestModel extends TwoModel {

	@Override
	public AutoTestCaseEntity getEntity() {
		return (AutoTestCaseEntity) super.getEntity();
	}

	/**
	 * @param proxy
	 * @param entity
	 */
	protected BaseAutoTestModel(ProxyService proxy, AutoTestCaseEntity entity) {
		super(proxy, entity);
	}

	/**
	 * @param proxy
	 */
	protected BaseAutoTestModel(ProxyService proxy) {
		super(proxy, new AutoTestCaseEntity());
	}

	/**
	 * check the result response matches the demand response, default just call equals
	 * 
	 * @param response
	 * @param demand
	 * @return
	 */
	protected boolean matches(String response, String demand) {
		// check full string
		if (response != null) {
			return response.equals(demand);
		} else {
			return false;
		}
	}

	/**
	 * get test response
	 * 
	 * @return
	 * @throws CoreException
	 */
	protected abstract String getTestResponse() throws CoreException;

	/**
	 * get test response and check matches
	 * 
	 * @throws TestException
	 */
	public void runTest() throws TestException {

		String resp = null;
		String demand = null;
		boolean testok = false;
		Exception lastex = null;

		// full try for any exception
		try {

			// get response
			resp = this.getTestResponse();

			// get demand response
			demand = this.getEntity().getDemandResponse();

			// check response
			if (this.matches(resp, demand)) {
				testok = true;
			}
		} catch (Exception ex) {
			lastex = ex;
		}

		// check result and write to result log
		try {
			AutoTestResultEntity result = new AutoTestResultEntity();
			result.setTestCaseId(this.getEntity().getId());
			result.setTestResponse(StringUtil.toString(resp, 4096));

			if (testok) {
				result.setTestResult("OK");
			} else if (lastex != null) {
				result.setTestResult("EXCEPTION");
				result.setTestResultMessage(StringUtil.toString(lastex, 256));
			} else {
				result.setTestResult("NOT MATCH");
			}

			// save result
			this.insertEntity(result);

		} catch (Exception ex) {
			throw new TestException("fatal error during write result log", ex);
		}

		// throw if not match
		if (!testok) {
			throw new TestException("response not match, demand=" + demand + ", response=" + resp);
		}
	}
}
