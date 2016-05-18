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
import org.yaen.starter.common.data.models.BaseAttributeModel;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.models.BaseRelationModel;
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
	private QueryMapper mapper;

	/**
	 * select ids by base id
	 * 
	 * @param element
	 * @param baseId
	 * @throws Exception
	 */
	@Override
	public <T_ATTR extends BaseAttributeModel> List<Long> SelectIDsByAttributeBase(T_ATTR attribute, long baseId)
			throws Exception {
		AssertUtil.notNull(attribute);

		// get another po
		AnotherEntity po = new AnotherEntity(attribute);

		// need event engine
		QueryEntity model = new QueryEntity();
		model.setTableName(po.getTableName());

		// set given base id
		model.setBaseId(baseId);

		// call event
		return mapper.selectIDsByBaseID(model);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByRelationFrom(org.yaen.starter.common.data.models.BaseRelationModel,
	 *      long)
	 */
	@Override
	public <T_REL extends BaseRelationModel> List<Long> SelectIDsByRelationFrom(T_REL rel, long fromId) throws Exception {
		AssertUtil.notNull(rel);

		// get another po
		AnotherEntity po = new AnotherEntity(rel);

		// need event engine
		QueryEntity model = new QueryEntity();
		model.setTableName(po.getTableName());

		// set given base id
		model.setFromId(fromId);

		// call event
		return mapper.selectIDsByFromID(model);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByRelationTo(org.yaen.starter.common.data.models.BaseRelationModel,
	 *      long)
	 */
	@Override
	public <T_REL extends BaseRelationModel> List<Long> SelectIDsByRelationTo(T_REL rel, long toId) throws Exception {
		AssertUtil.notNull(rel);

		// get another po
		AnotherEntity po = new AnotherEntity(rel);

		// need event engine
		QueryEntity model = new QueryEntity();
		model.setTableName(po.getTableName());

		// set given base id
		model.setFromId(toId);

		// call event
		return mapper.selectIDsByToID(model);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByFieldName(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<Long> SelectIDsByFieldName(T model, String fieldName) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(fieldName);
		return this.SelectIDsByFieldNameList(model, list);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByFieldNameList(org.yaen.starter.common.data.models.BaseModel,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseModel> List<Long> SelectIDsByFieldNameList(T model, List<String> fieldNameList)
			throws Exception {
		AssertUtil.notNull(model);
		AssertUtil.notNull(fieldNameList);

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
		return mapper.selectIDsByColumns(query);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByAllField(org.yaen.starter.common.data.models.BaseModel)
	 */
	@Override
	public <T extends BaseModel> List<Long> SelectIDsByAllField(T model) throws Exception {
		AssertUtil.notNull(model);

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
		return mapper.selectIDsByColumns(query);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByWhereClause(org.yaen.starter.common.data.models.BaseModel,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseModel> List<Long> SelectIDsByWhereClause(T model, String whereClause) throws Exception {
		AssertUtil.notNull(model);
		AssertUtil.notNull(whereClause);

		// get another po
		AnotherEntity po = new AnotherEntity(model);

		// make event model
		QueryEntity query = new QueryEntity();

		// set table name
		query.setTableName(po.getTableName());

		// set where clause
		query.setWhereClause(whereClause);

		// call mapper
		return mapper.selectIDsByWhereClause(query);
	}

}
