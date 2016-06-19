package org.yaen.starter.core.model.services;

import java.util.List;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.models.BaseModel;

/**
 * query service for most search operation
 * <p>
 * all result is id list, and then can page in application level
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface QueryService {

	/**
	 * select model list by given rowid list, with triggers
	 * 
	 * @param model
	 * @param rowids
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<T> selectModelListByRowids(T model, List<Long> rowids) throws CoreException;

	/**
	 * select model list by given id, with triggers
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<T> selectModelListById(T model, String id) throws CoreException;

	/**
	 * select value list by given id and field name, no triggers
	 * 
	 * @param model
	 * @param id
	 * @param fieldName
	 * @return
	 * @throws CoreException
	 */
	public <T extends BaseModel> List<Object> selectValueListById(T model, String id, String fieldName)
			throws CoreException;

	/**
	 * select rowids by field name, the value is the given value in the model and use equal, no triggers
	 * 
	 * @param model
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowidsByFieldName(T model, String fieldName) throws CoreException;

	/**
	 * select rowids by field name list, the value is the given value in the model and use equal, no triggers
	 * 
	 * @param model
	 * @param fieldNameList
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowidsByFieldNameList(T model, List<String> fieldNameList)
			throws CoreException;

	/**
	 * select rowids by all none-null field, the value is the given value in the model, no triggers
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowidsByAllField(T model) throws CoreException;

	/**
	 * select rowids by given sql, starting with where, can include order, group, and having clause, no triggers
	 * 
	 * @param model
	 * @param whereClause
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowsByWhereClause(T model, String whereClause) throws CoreException;

	/**
	 * select rowids by all, no triggers
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<Long> selectRowidsByAll(T model) throws CoreException;

}
