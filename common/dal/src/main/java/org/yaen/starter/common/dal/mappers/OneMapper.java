package org.yaen.starter.common.dal.mappers;

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

	public Map<String, Object> selectByID(OneEntity po) throws Exception;

	public int insertByID(OneEntity po) throws Exception;

	public int updateByID(OneEntity po) throws Exception;

	public int deleteByID(OneEntity po) throws Exception;

}
