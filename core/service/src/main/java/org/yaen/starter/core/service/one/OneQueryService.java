package org.yaen.starter.core.service.one;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.dal.mappers.QueryMapper;
import org.yaen.starter.common.data.entities.AnotherEntity;
import org.yaen.starter.common.data.entities.OneColumnEntity;
import org.yaen.starter.common.data.entities.OneEntity;
import org.yaen.starter.common.data.entities.QueryEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.OperationCancelledCoreException;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.service.utils.OneServiceUil;

/**
 * one event service for most operation
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class OneQueryService implements QueryService {

	@Autowired
	private QueryMapper queryMapper;

	/**
	 * inner select model list, no triggers, origin model is not changed
	 * 
	 * @param model
	 * @param rowids
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseModel> List<T> innerSelectModelListByRowids(T model, List<Long> rowids, OneEntity entity)
			throws CoreException {

		// return empty if rowids is empty
		if (rowids.isEmpty()) {
			return new ArrayList<T>();
		}

		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);
			}

			// set rowid list
			entity.setRowids(rowids);

			// call mapper
			List<Map<String, Object>> maps = queryMapper.selectListByRowids(entity);

			// check existence
			if (maps == null || maps.isEmpty()) {
				return new ArrayList<T>();
			}

			List<T> list = new ArrayList<T>(maps.size());

			// make models
			for (Map<String, Object> map : maps) {

				// create new model
				T newmodel = (T) model.getClass().newInstance();

				// fill field
				OneServiceUil.fillModelByColumns(newmodel, entity.getColumns(), map);

				list.add(newmodel);
			}
			return list;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner select model by id, no triggers, origin model is not changed
	 * 
	 * @param model
	 * @param rowids
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseModel> List<T> innerSelectModelListById(T model, String id, OneEntity entity)
			throws CoreException {
		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);
			}

			// set id
			entity.setId(id);

			// call mapper
			List<Map<String, Object>> maps = queryMapper.selectListById(entity);

			// check existence
			if (maps == null) {
				return null;
			}

			List<T> list = new ArrayList<T>(maps.size());

			// make models
			for (Map<String, Object> map : maps) {

				// create new model
				T newmodel = (T) model.getClass().newInstance();

				// fill field
				OneServiceUil.fillModelByColumns(newmodel, entity.getColumns(), map);

				list.add(newmodel);
			}
			return list;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * inner select value by id, no triggers, origin model is not changed
	 * 
	 * @param model
	 * @param rowids
	 * @param fieldName
	 * @param entity
	 * @return
	 * @throws CoreException
	 */
	protected <T extends BaseModel> List<Object> innerSelectValueListById(T model, String id, String fieldName,
			OneEntity entity) throws CoreException {
		try {
			// get another entity if not
			if (entity == null) {
				entity = new AnotherEntity(model);
			}

			// set id and column
			entity.setId(id);
			entity.setSelectedColumnName(entity.getColumns().get(fieldName).getColumnName());

			// call mapper
			List<Object> values = queryMapper.selectValueListById(entity);

			return values;
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#selectModelListByRowids(org.yaen.starter.common.data.models.BaseModel,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseModel> List<T> selectModelListByRowids(T model, List<Long> rowids) throws CoreException {
		AssertUtil.notNull(model);
		AssertUtil.notNull(rowids);

		// trigger before select, only once is ok
		if (model.BeforeSelect()) {

			List<T> list = this.innerSelectModelListByRowids(model, rowids, null);

			// trigger after select each
			for (int i = 0; i < list.size(); i++) {
				list.get(i).AfterSelect();
			}

			return list;

		} else {
			// select canceled
			throw new OperationCancelledCoreException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#selectModelListById(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<T> selectModelListById(T model, String id) throws CoreException {
		AssertUtil.notNull(model);

		// trigger before select, only once is ok
		if (model.BeforeSelect()) {

			List<T> list = this.innerSelectModelListById(model, id, null);

			// trigger after select each
			for (int i = 0; i < list.size(); i++) {
				list.get(i).AfterSelect();
			}

			return list;
		} else {
			// select canceled
			throw new OperationCancelledCoreException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.ModelService#selectValueListById(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<Object> selectValueListById(T model, String id, String fieldName)
			throws CoreException {
		AssertUtil.notNull(model);
		AssertUtil.notBlank(fieldName);

		// trigger before select, only once is ok
		if (model.BeforeSelect()) {

			List<Object> list = this.innerSelectValueListById(model, id, fieldName, null);

			return list;
		} else {
			// select canceled
			throw new OperationCancelledCoreException("select cancelled by trigger");
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByFieldName(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<Long> selectRowidsByFieldName(T model, String fieldName) throws CoreException {
		List<String> list = new ArrayList<String>();
		list.add(fieldName);
		return this.selectRowidsByFieldNameList(model, list);
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByFieldNameList(org.yaen.starter.common.data.models.BaseModel,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseModel> List<Long> selectRowidsByFieldNameList(T model, List<String> fieldNameList)
			throws CoreException {
		AssertUtil.notNull(model);
		AssertUtil.notNull(fieldNameList);

		try {
			// get another po
			AnotherEntity po = new AnotherEntity(model);

			// make event model
			QueryEntity query = new QueryEntity();

			// set table name
			query.setTableName(po.getTableName());

			// make columns
			{
				Map<String, Object> columns = new HashMap<String, Object>();

				for (String fieldname : fieldNameList) {

					OneColumnEntity info = po.getColumns().get(fieldname);
					columns.put(info.getColumnName(), info.getValue());
				}

				query.setColumns(columns);
			}

			// call mapper
			return queryMapper.selectRowidsByColumns(query);
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByAllField(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> List<Long> selectRowidsByAllField(T model) throws CoreException {
		AssertUtil.notNull(model);

		try {
			// get another po
			AnotherEntity po = new AnotherEntity(model);

			// make event model
			QueryEntity query = new QueryEntity();

			// set table name
			query.setTableName(po.getTableName());

			// make columns
			{
				Map<String, Object> columns = new HashMap<String, Object>();

				// add columns if not id and is not null
				for (Entry<String, OneColumnEntity> entry : po.getColumns().entrySet()) {
					if (!StringUtil.like(entry.getKey(), "id") && entry.getValue().getValue() != null) {
						columns.put(entry.getValue().getColumnName(), entry.getValue().getValue());
					}
				}

				query.setColumns(columns);
			}

			// call mapper
			return queryMapper.selectRowidsByColumns(query);
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowsByWhereClause(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<Long> selectRowsByWhereClause(T model, String whereClause) throws CoreException {
		AssertUtil.notNull(model);
		AssertUtil.notNull(whereClause);

		try {
			// get another po
			AnotherEntity po = new AnotherEntity(model);

			// make event model
			QueryEntity query = new QueryEntity();

			// set table name
			query.setTableName(po.getTableName());

			// set where clause
			query.setWhereClause(whereClause);

			// call mapper
			return queryMapper.selectRowidsByWhereClause(query);
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#selectRowidsByAll(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> List<Long> selectRowidsByAll(T model) throws CoreException {
		AssertUtil.notNull(model);

		try {
			// get another po
			AnotherEntity po = new AnotherEntity(model);

			// make event model
			QueryEntity query = new QueryEntity();

			// set table name
			query.setTableName(po.getTableName());

			// call mapper
			return queryMapper.selectRowidsByAll(query);
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

}
