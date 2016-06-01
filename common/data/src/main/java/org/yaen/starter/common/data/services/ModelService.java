package org.yaen.starter.common.data.services;

import java.util.List;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.models.BaseModel;

/**
 * model service for CRUD
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface ModelService {

	/**
	 * select model by rowid, and update model object with data
	 * 
	 * @param model
	 * @param rowid
	 * @throws Exception
	 */
	public <T extends BaseModel> void selectModelByRowid(T model, long rowid) throws CoreException;

	/**
	 * insert model
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> long insertModelByRowid(T model) throws CoreException;

	/**
	 * update model by rowid, this id is also be updated
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> void updateModelByRowid(T model) throws CoreException;

	/**
	 * delete model by rowid
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> void deleteModelByRowid(T model) throws CoreException;

	/**
	 * try select model by rowid, return false if not exists
	 * 
	 * @param model
	 * @param rowid
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> boolean trySelectModelByRowid(T model, long rowid) throws CoreException;

	/**
	 * select model list by given rowid list
	 * 
	 * @param model
	 * @param rowids
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<T> selectModelListByRowids(T model, List<Long> rowids) throws CoreException;

	/**
	 * select model list by given id
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<T> selectModelListById(T model, String id) throws CoreException;

	/**
	 * select value list by given id and field name
	 * 
	 * @param model
	 * @param id
	 * @param fieldName
	 * @return
	 * @throws CoreException
	 */
	public <T extends BaseModel> List<Object> selectValueListById(T model, String id, String fieldName)
			throws CoreException;

}
