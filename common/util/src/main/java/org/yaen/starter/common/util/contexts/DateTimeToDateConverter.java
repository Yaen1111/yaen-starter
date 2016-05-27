package org.yaen.starter.common.util.contexts;

import java.util.Date;

import org.joda.time.DateTime;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * convert joda datetime to date
 * 
 * @author Yaen 2016年5月27日上午11:35:34
 */
public class DateTimeToDateConverter implements Converter<DateTime, Date> {

	/**
	 * convert joda datetime to date
	 * 
	 * @see org.modelmapper.Converter#convert(org.modelmapper.spi.MappingContext)
	 */
	@Override
	public Date convert(MappingContext<DateTime, Date> context) {

		DateTime source = context.getSource();
		if (source != null) {
			return source.toDate();
		}
		return null;
	}
}
