package org.yaen.starter.common.dal.mappers;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.yaen.starter.common.dal.entities.QueryEntity;

/**
 * event mapper for event engine, most of operation return id list for memery paging
 * 
 * @author Yaen 2016年1月6日下午7:51:57
 */
@Repository
public interface QueryMapper {

	/**
	 * get rowid list by given column and value
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<Long> selectRowidsByColumns(QueryEntity query) throws Exception;

	/**
	 * get rowid list by given where clause
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<Long> selectRowidsByWhereClause(QueryEntity query) throws Exception;

	/**
	 * get rowid list by all
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<Long> selectRowidsByAll(QueryEntity query) throws Exception;

}
