package org.yaen.starter.common.dal.mappers;

import java.util.Map;

import org.springframework.stereotype.Repository;
import org.yaen.starter.common.dal.entities.OneEntity;

/**
 * one mapper for all crud operations
 * <p>
 * prepare for WRITE/MAIN/MASTER db server
 * 
 * @author Yaen 2016年5月3日下午9:28:37
 */
@Repository
public interface EntityMapper {

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

}
