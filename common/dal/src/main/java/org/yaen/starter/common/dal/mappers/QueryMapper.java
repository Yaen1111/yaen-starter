package org.yaen.starter.common.dal.mappers;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.yaen.starter.common.dal.models.QueryModel;

/**
 * event mapper for event engine
 * 
 * 
 * @author Yaen 2016年1月6日下午7:51:57
 */
@Repository
public interface QueryMapper {

	/**
	 * get id list by base id of attribute entity
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<Long> selectIDsByBaseID(QueryModel model) throws Exception;

	/**
	 * get id list by relation from id, usually is the child/person
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<Long> selectIDsByFromID(QueryModel model) throws Exception;

	/**
	 * get id list by relation from id, usually is the parent/org
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<Long> selectIDsByToID(QueryModel model) throws Exception;

	/**
	 * get id list by given column and value
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<Long> selectIDsByColumns(QueryModel model) throws Exception;

	/**
	 * get id list by given where clause
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<Long> selectIDsByWhereClause(QueryModel model) throws Exception;

}
