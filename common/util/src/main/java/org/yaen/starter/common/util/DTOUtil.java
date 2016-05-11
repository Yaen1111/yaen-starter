package org.yaen.starter.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * data copy util
 * 
 * 
 * @author Yaen 2016年1月5日下午10:24:22
 */
public class DTOUtil {

	public static final Logger log = LoggerFactory.getLogger(DTOUtil.class);

	public static final ModelMapper INSTANCE = new ModelMapper();

	static {
		INSTANCE.addConverter(new DateTimeToDateConverter());
		INSTANCE.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}

	public static <S, T> T map(S source, Class<T> targetClass) {

		return INSTANCE.map(source, targetClass);
	}

	public static <S, T> void map(S source, T dist) {

		INSTANCE.map(source, dist);
	}

	public static <S, T> List<T> map(List<S> source, Class<T> targetClass) {

		List<T> list = new ArrayList<T>(source.size());
		for (Object obj : source) {
			T target = (T) INSTANCE.map(obj, targetClass);
			list.add(target);
		}
		return list;
	}

	public static Map<String, Object> beanToMap(Object source) throws Exception {
		if (source == null) {
			throw new Exception("beanToMap source bean is null");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fds = source.getClass().getDeclaredFields();
		for (Field f : fds) {
			f.setAccessible(true);
			map.put(f.getName(), f.get(source));
		}
		return map;
	}

	private static class DateTimeToDateConverter implements Converter<DateTime, Date> {
		@Override
		public Date convert(MappingContext<DateTime, Date> context) {

			DateTime source = context.getSource();
			if (source != null) {
				return source.toDate();
			}
			return null;
		}
	}

}
