/**
 * 
 */
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
	 * select model by id, and update model object with data
	 * 
	 * @param model
	 * @param id
	 * @throws Exception
	 */
	public <T extends BaseModel> void selectModel(T model, long id) throws CoreException;

	/**
	 * try select model, return false if not exists
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> boolean trySelectModel(T model, long id) throws CoreException;

	/**
	 * select model list by given id list
	 * 
	 * @param model
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> List<T> selectModelList(T model, List<Long> ids) throws CoreException;

	/**
	 * insert model
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> long insertModel(T model) throws CoreException;

	/**
	 * update model
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> void updateModel(T model) throws CoreException;

	/**
	 * delete model
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> void deleteModel(T model) throws CoreException;

}
