package org.yaen.starter.core.model.autotest.models;

import java.util.ArrayList;
import java.util.List;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataTypeUnknownException;
import org.yaen.starter.common.data.exceptions.TestException;
import org.yaen.starter.common.data.objects.Dim3;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.autotest.entities.AutoTestCaseEntity;
import org.yaen.starter.core.model.autotest.enums.TestTypes;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * batch auto test model, load test by group or list
 * 
 * @author Yaen 2016年8月2日上午10:59:45
 */
@Slf4j
public class BatchAutoTestModel extends TwoModel {

	@Override
	public AutoTestCaseEntity getEntity() {
		return (AutoTestCaseEntity) super.getEntity();
	}

	/** the entity list */
	@Getter
	private List<AutoTestCaseEntity> entityList = new ArrayList<AutoTestCaseEntity>();

	/**
	 * make test model according to test case entity, prepare to be override
	 * 
	 * @param entity
	 * @return
	 * @throws DataTypeUnknownException
	 */
	protected BaseAutoTestModel makeTestModel(AutoTestCaseEntity entity) throws DataTypeUnknownException {
		// get type for switch, should not be null
		String test_type = entity.getTestType();
		if (test_type == null)
			test_type = "";

		switch (test_type) {
		case TestTypes.TEST_HTTP_GET:
		case TestTypes.TEST_HTTPS_GET:
		case TestTypes.TEST_HTTP_POST:
		case TestTypes.TEST_HTTPS_POST:
			return new HttpAutoTestModel(this.proxy, entity);
		default:
			throw new DataTypeUnknownException("unknown type " + test_type);
		}
	}

	/**
	 * @param proxy
	 * @param entity
	 */
	public BatchAutoTestModel(ProxyService proxy) {
		super(proxy, new AutoTestCaseEntity());
	}

	/**
	 * load list by group
	 * 
	 * @param testGroup
	 * @throws DataException
	 * @throws CommonException
	 */
	public void loadByGroup(String testGroup) throws DataException, CommonException {
		AssertUtil.notBlank(testGroup);

		// set id to entity
		this.getEntity().setTestGroup(testGroup);
		List<Long> rowids = this.proxy.getQueryService().selectRowidsByField(this.getEntity(), "testGroup");
		List<AutoTestCaseEntity> list = this.proxy.getQueryService().selectListByRowids(this.getEntity(), rowids);

		// replace entity list
		this.entityList.clear();
		if (list != null && !list.isEmpty()) {
			this.entityList.addAll(list);
		}
	}

	/**
	 * run batch test, throw if any one fail, return count of each result type
	 * 
	 * @return
	 * @throws TestException
	 */
	public Dim3<Integer, Integer, Integer> runBatchTest() throws TestException {

		int ok = 0;
		int fail = 0;
		int error = 0;

		// run test for each test case
		for (AutoTestCaseEntity test : this.entityList) {
			try {
				BaseAutoTestModel testmodel = this.makeTestModel(test);
				testmodel.runTest();

				// is ok
				ok++;
			} catch (TestException ex) {
				log.warn("test fail", ex);

				// test fail
				fail++;
			} catch (Exception ex) {
				log.error("test error", ex);

				// test error
				error++;
			}
		} // for

		// write log
		log.info("runBatchTest result, ok={}, fail={}, error={}", ok, fail, error);

		// return result
		return new Dim3<Integer, Integer, Integer>(ok, fail, error);
	}

}
