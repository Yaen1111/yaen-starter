package org.yaen.starter.test.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.yaen.starter.common.dal.mappers.TableMapper;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;

/**
 * 
 * 
 * @author Yaen 2016年5月23日下午4:50:16
 */
public class TableDescribeTester extends StarterUnitTester {

	@Autowired
	private TableMapper zero;

	@org.junit.Test
	public void Test() {

		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");

		try {

			// get all table
			List<String> tables = zero.showTables();
			for (String tablename : tables) {
				if (tablename.startsWith("xlc_") || tablename.startsWith("te_")) {

					// get columns
					// List<MyDescribeEntity> columns = zero.describeTable(tablename);

				}

			}

		} catch (CommonException ex) {
			System.out.println(ex);
		} catch (CoreException ex) {
			System.out.println(ex);
		} catch (BizException ex) {
			System.out.println(ex);
		} catch (Exception ex) {
			System.out.println(ex);
		}

		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
	}

}
