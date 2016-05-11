/**
 * 
 */
package org.yaen.starter.common.data.services;

import java.util.List;

import org.yaen.starter.common.data.entities.AttributeEntity;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.entities.RelationEntity;

/**
 * query service for most search operation
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
	public <T_ATTR extends AttributeEntity> List<Long> SelectIDsByAttributeBase(T_ATTR attribute, long baseId)
			throws Exception;

	/**
	 * select relation ids by from id
	 * 
	 * @param rel
	 * @param fromId
	 * @return
	 * @throws Exception
	 */
	public <T_REL extends RelationEntity> List<Long> SelectIDsByRelationFrom(T_REL rel, long fromId) throws Exception;

	/**
	 * select relation ids by to id
	 * 
	 * @param rel
	 * @param toId
	 * @return
	 * @throws Exception
	 */
	public <T_REL extends RelationEntity> List<Long> SelectIDsByRelationTo(T_REL rel, long toId) throws Exception;

	/**
	 * select ids by field name, the value is the given value in the entity and
	 * use equal
	 * 
	 * @param entity
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseEntity> List<Long> SelectIDsByFieldName(T entity, String fieldName) throws Exception;

	/**
	 * select ids by field name list, the value is the given value in the entity
	 * and use equal
	 * 
	 * @param entity
	 * @param fieldNameList
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseEntity> List<Long> SelectIDsByFieldNameList(T entity, List<String> fieldNameList)
			throws Exception;

	/**
	 * select ids by all none-null field, the value is the given value in the
	 * entity
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseEntity> List<Long> SelectIDsByAllField(T entity) throws Exception;

	/**
	 * select ids by given sql, starting with where, can include order, group,
	 * and having clause
	 * 
	 * @param entity
	 * @param whereClause
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseEntity> List<Long> SelectIDsByWhereClause(T entity, String whereClause) throws Exception;

}
