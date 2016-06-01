package org.yaen.starter.common.data.services;

import java.util.List;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.models.BaseModel;

/**
 * query service for most search operation
 * <p>
 * all result is id list, and then can page in application level
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface QueryService {

	/**
	 * select rowids by field name, the value is the given value in the model and use equal
	 * 
	 * @param model
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowidsByFieldName(T model, String fieldName) throws CoreException;

	/**
	 * select rowids by field name list, the value is the given value in the model and use equal
	 * 
	 * @param model
	 * @param fieldNameList
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowidsByFieldNameList(T model, List<String> fieldNameList)
			throws CoreException;

	/**
	 * select rowids by all none-null field, the value is the given value in the model
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowidsByAllField(T model) throws CoreException;

	/**
	 * select rowids by given sql, starting with where, can include order, group, and having clause
	 * 
	 * @param model
	 * @param whereClause
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowsByWhereClause(T model, String whereClause) throws CoreException;

	/**
	 * select rowids by all
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowidsByAll(T model) throws CoreException;

}
