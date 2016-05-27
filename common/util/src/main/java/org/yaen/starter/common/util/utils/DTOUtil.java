package org.yaen.starter.common.util.utils;

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

/**
 * data copy util
 * 
 * @author Yaen 2016年1月5日下午10:24:22
 */
public class DTOUtil {

	/** model mapper */
	public static final ModelMapper INSTANCE = new ModelMapper();

	/**
	 * static constructor
	 */
	static {
		// add a joda datetime to date converter
		INSTANCE.addConverter(new DateTimeToDateConverter());
		INSTANCE.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}

	/**
	 * copy source to target class, a new object will be created
	 * 
	 * @param source
	 * @param targetClass
	 * @return
	 */
	public static <S, T> T map(S source, Class<T> targetClass) {
		return INSTANCE.map(source, targetClass);
	}

	/**
	 * copy object data from source to target
	 * 
	 * @param source
	 * @param target
	 */
	public static <S, T> void map(S source, T target) {

		INSTANCE.map(source, target);
	}

	/**
	 * copy object list to target class list
	 * 
	 * @param source
	 * @param targetClass
	 * @return
	 */
	public static <S, T> List<T> map(List<S> source, Class<T> targetClass) {

		List<T> list = new ArrayList<T>(source.size());
		for (Object obj : source) {
			T target = (T) INSTANCE.map(obj, targetClass);
			list.add(target);
		}
		return list;
	}

	/**
	 * map object fields to map, but with out parent fields
	 * 
	 * @param source
	 * @return
	 */
	public static Map<String, Object> map(Object source) {
		AssertUtil.notNull(source, "source object is null");

		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fds = source.getClass().getDeclaredFields();
		for (Field f : fds) {
			f.setAccessible(true);
			// ignore access error
			try {
				map.put(f.getName(), f.get(source));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
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
