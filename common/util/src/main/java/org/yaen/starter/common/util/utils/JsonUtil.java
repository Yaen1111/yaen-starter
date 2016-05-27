package org.yaen.starter.common.util.utils;

import java.util.Arrays;
import java.util.List;

import org.yaen.starter.common.util.contexts.FastJsonFilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * fast json convert util
 * 
 * @author Yaen 2016年5月27日上午10:47:22
 */
public class JsonUtil {

	/**
	 * serialize to json string, all null value is omitted
	 * 
	 * @param object
	 * @return
	 */
	public static <T> String serialize(T object) {
		return JSON.toJSONString(object);
	}

	/**
	 * serialize to json string, with filters
	 * 
	 * @param object
	 * @param includes
	 * @return
	 */
	public static <T> String serialize(T object, String[] includes) {
		// no filter, use default
		if (includes == null || includes.length == 0) {
			return serialize(object);
		}

		// make filter
		FastJsonFilter filter = new FastJsonFilter();
		filter.getIncludes().addAll(Arrays.<String> asList(includes));

		return JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat);
	}

	/**
	 * serialize to json string, with filters
	 * 
	 * @param object
	 * @param includes
	 * @param excludes
	 * @return
	 */
	public static <T> String serialize(T object, String[] includes, String[] excludes) {
		// no filter, use default
		if (excludes == null || excludes.length == 0) {
			return serialize(object, includes);
		}

		// make filter
		FastJsonFilter filter = new FastJsonFilter();
		filter.getExcludes().addAll(Arrays.asList(excludes));

		// add includes if not empty
		if (includes != null && includes.length >= 0) {
			filter.getIncludes().addAll(Arrays.asList(includes));
		}

		return JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat);
	}

	/**
	 * serialize to json string, all null value written as empty string
	 * 
	 * @param object
	 * @return
	 */
	public static <T> String serializeWithNull(T object) {
		return JSON.toJSONString(object, new ValueFilter() {
			@Override
			public Object process(Object obj, String s, Object v) {
				if (v == null)
					return "";
				return v;
			}
		}, SerializerFeature.WriteNullStringAsEmpty);
	}

	/**
	 * deserialize from json string
	 * 
	 * @param json
	 * @param clz
	 * @return
	 */
	public static <T> T deserialize(String json, Class<T> clz) {
		return JSON.parseObject(json, clz);
	}

	/**
	 * deserialize list from json string
	 * 
	 * @param json
	 * @param clz
	 * @return
	 */
	public static <T> List<T> deserializeList(String json, Class<T> clz) {
		return JSON.parseArray(json, clz);
	}

	/**
	 * 将JSON格式的字符串转换成任意Java类型的对象
	 * 
	 * @param json
	 *            JSON格式的字符串
	 * @param type
	 *            任意Java类型
	 * @return 任意Java类型的对象
	 */
	public static <T> T deserializeAny(String json, TypeReference<T> type) {
		return JSON.parseObject(json, type);
	}

}
