/**
 * 
 */
package org.yaen.starter.common.data.services;

import java.util.List;

import org.yaen.starter.common.data.models.BaseAttributeModel;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.models.BaseRelationModel;

/**
 * query service for most search operation
 * <p>
 * all result is id list, and then can page in application level
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface QueryService {

	/**
	 * select attribute ids by base id
	 * 
	 * @param attribute
	 * @param baseId
	 * @return
	 * @throws Exception
	 */
	public <T_ATTR extends BaseAttributeModel> List<Long> SelectIDsByAttributeBase(T_ATTR attribute, long baseId)
			throws Exception;

	/**
	 * select relation ids by from id
	 * 
	 * @param rel
	 * @param fromId
	 * @return
	 * @throws Exception
	 */
	public <T_REL extends BaseRelationModel> List<Long> SelectIDsByRelationFrom(T_REL rel, long fromId)
			throws Exception;

	/**
	 * select relation ids by to id
	 * 
	 * @param rel
	 * @param toId
	 * @return
	 * @throws Exception
	 */
	public <T_REL extends BaseRelationModel> List<Long> SelectIDsByRelationTo(T_REL rel, long toId) throws Exception;

	/**
	 * select ids by field name, the value is the given value in the model and use equal
	 * 
	 * @param model
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> SelectIDsByFieldName(T model, String fieldName) throws Exception;

	/**
	 * select ids by field name list, the value is the given value in the model and use equal
	 * 
	 * @param model
	 * @param fieldNameList
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> SelectIDsByFieldNameList(T model, List<String> fieldNameList)
			throws Exception;

	/**
	 * select ids by all none-null field, the value is the given value in the model
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> SelectIDsByAllField(T model) throws Exception;

	/**
	 * select ids by given sql, starting with where, can include order, group, and having clause
	 * 
	 * @param model
	 * @param whereClause
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> SelectIDsByWhereClause(T model, String whereClause) throws Exception;

}
