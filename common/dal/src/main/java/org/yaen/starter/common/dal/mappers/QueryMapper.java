package org.yaen.starter.common.dal.mappers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.yaen.starter.common.data.entities.OneEntity;
import org.yaen.starter.common.data.entities.QueryEntity;

/**
 * event mapper for event engine, most of operation return id list for memery paging
 * <p>
 * prepare for READONLY/SUB/SLAVE db server
 * 
 * @author Yaen 2016年1月6日下午7:51:57
 */
@Repository
public interface QueryMapper {

	/**
	 * select object list by rowid list
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectListByRowids(OneEntity entity) throws Exception;

	/**
	 * select object list by id
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectListById(OneEntity entity) throws Exception;

	/**
	 * select object list by id
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectValueListById(OneEntity entity) throws Exception;

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
