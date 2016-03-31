package org.yaen.ark.common.dal.mappers;

import java.util.Map;

import org.springframework.stereotype.Repository;
import org.yaen.ark.common.dal.models.ElementModel;


/**
 * element mapper for none-type query
 * 
 * 
 * @author xl 2016年1月6日下午7:51:57
 */
@Repository
public interface ElementMapper {

	public Map<String, Object> selectByID(ElementModel model) throws Exception;

	public int insertByID(ElementModel model) throws Exception;

	public int updateByID(ElementModel model) throws Exception;

	public int deleteByID(ElementModel model) throws Exception;

}
