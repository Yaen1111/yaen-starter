package org.yaen.starter.common.dal.mappers;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.yaen.starter.common.data.pos.MyDescribePO;
import org.yaen.starter.common.data.pos.OnePO;

/**
 * zero mapper for table create
 * 
 * @author Yaen 2016年1月6日下午7:51:57
 */
@Repository
public interface ZeroMapper {

	/**
	 * create table by given element info
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	public int createTable(OnePO po) throws Exception;

	/**
	 * get table columns info by given table name
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	public List<MyDescribePO> describeTable(OnePO po) throws Exception;

	/**
	 * add given column, usually is in the last
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	public int addColumn(OnePO po) throws Exception;

	/**
	 * modify given column, usually is in the last
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	public int modifyColumn(OnePO po) throws Exception;

}
