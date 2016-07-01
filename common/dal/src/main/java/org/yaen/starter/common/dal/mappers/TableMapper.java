package org.yaen.starter.common.dal.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.yaen.starter.common.dal.entities.MyDescribeEntity;
import org.yaen.starter.common.dal.entities.OneEntity;

/**
 * zero mapper for table create
 * 
 * @author Yaen 2016年1月6日下午7:51:57
 */
@Repository
public interface TableMapper {

	/**
	 * create table by given element info
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	public int createTable(OneEntity po) throws Exception;

	/**
	 * show all tables
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> showTables() throws Exception;

	/**
	 * get table columns info by given table name
	 * 
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public List<MyDescribeEntity> describeTable(@Param("tableName") String tableName) throws Exception;

	/**
	 * add given column, usually is in the last
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	public int addColumn(OneEntity po) throws Exception;

	/**
	 * modify given column, usually is in the last
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	public int modifyColumn(OneEntity po) throws Exception;

}
