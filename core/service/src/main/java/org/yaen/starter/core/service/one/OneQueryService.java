package org.yaen.starter.core.service.one;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.dal.entities.QueryEntity;
import org.yaen.starter.common.dal.mappers.QueryMapper;
import org.yaen.starter.common.data.entities.AnotherEntity;
import org.yaen.starter.common.data.entities.OneColumnEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;

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
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByFieldName(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<Long> SelectIDsByFieldName(T model, String fieldName) throws CoreException {
		List<String> list = new ArrayList<String>();
		list.add(fieldName);
		return this.SelectIDsByFieldNameList(model, list);
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByFieldNameList(org.yaen.starter.common.data.models.BaseModel,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseModel> List<Long> SelectIDsByFieldNameList(T model, List<String> fieldNameList)
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
			return queryMapper.selectIDsByColumns(query);
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByAllField(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> List<Long> SelectIDsByAllField(T model) throws CoreException {
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
			return queryMapper.selectIDsByColumns(query);
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByWhereClause(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<Long> SelectIDsByWhereClause(T model, String whereClause) throws CoreException {
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
			return queryMapper.selectIDsByWhereClause(query);
		} catch (Exception ex) {
			throw new CoreException(ex);
		}
	}

}
