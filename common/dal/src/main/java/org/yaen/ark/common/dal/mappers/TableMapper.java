package org.yaen.ark.common.dal.mappers;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.yaen.ark.common.dal.models.DescribeModel;
import org.yaen.ark.common.dal.models.ElementModel;

/**
 * table mapper for table management
 * 
 * 
 * @author xl 2016年1月6日下午7:51:57
 */
@Repository
public interface TableMapper {

	/**
	 * create table by given element info
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int createTable(ElementModel model) throws Exception;

	/**
	 * get table columns info by given table name
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<DescribeModel> describeTable(ElementModel model) throws Exception;

	/**
	 * add given column, usually is in the last
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int addColumn(ElementModel model) throws Exception;

	/**
	 * modify given column, usually is in the last
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int modifyColumn(ElementModel model) throws Exception;

}
