package org.yaen.starter.common.dal.mappers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.yaen.starter.common.data.entities.OneEntity;

/**
 * one mapper for all crud operations
 * 
 * @author Yaen 2016年5月3日下午9:28:37
 */
@Repository
public interface OneMapper {

	/**
	 * select one object by rowid
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectByRowid(OneEntity entity) throws Exception;

	/**
	 * insert by rowid
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public int insertByRowid(OneEntity entity) throws Exception;

	/**
	 * update by rowid
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public int updateByRowid(OneEntity entity) throws Exception;

	/**
	 * delete by rowid
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public int deleteByRowid(OneEntity entity) throws Exception;

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

}
