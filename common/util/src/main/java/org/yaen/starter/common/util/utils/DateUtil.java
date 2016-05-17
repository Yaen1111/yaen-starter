package org.yaen.starter.common.util.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * date util
 * 
 * 
 * @author Yaen 2016年1月5日下午1:26:56
 */
public class DateUtil {

	/** yyyy-MM-dd */
	public static final String DATE_FMT = "yyyy-MM-dd";

	/** yyyy-MM-dd HH:mm:ss */
	public static final String DATETIME_FMT = "yyyy-MM-dd HH:mm:ss";

	/** HH:mm:ss */
	public static final String TIME_FMT = "HH:mm:ss";

	/** yyyyMMdd */
	public static final String DATE_TPT_TWO = "yyyyMMdd";

	/** yyyyMM */
	public static final String DATE_TPT_THREE = "yyyy-MM";

	/**
	 * make date by given info
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date makeDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month - 1, day);
		return calendar.getTime();
	}

	/**
	 * make date by given info
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date makeDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month - 1, day, hour, minute, second);
		return calendar.getTime();
	}

	/**
	 * get now date time
	 * 
	 * @return
	 */
	public static Date getNow() {
		return new Date();
	}

	/**
	 * get infinite date time, stands for 9999-12-31 23:59:59
	 * 
	 * @return
	 */
	public static Date getInfinite() {
		return makeDate(9999, 12, 31, 23, 59, 59);
	}

	/**
	 * 根据制定日期格式获取上个月的日期
	 *
	 * @return
	 */
	public static String getLastMonthDate() {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat(DATE_TPT_THREE);
		String time = format.format(c.getTime());
		return time;
	}

	/**
	 * 讲当前日期返回，格式：yyyyMMddHHmmssS
	 *
	 * @author bb-he
	 * @return String 日期字符串
	 */
	public static String formatDateSeq() {

		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FMT);
		return sdf.format(new Date());
	}

	/**
	 * 将日期转换成指定格式字符串
	 *
	 * @param date
	 * @param str
	 * @return
	 */
	public static String formatDate(Date date, String str) {

		SimpleDateFormat sdf = new SimpleDateFormat(str);
		return sdf.format(date);
	}

	/**
	 * 将日期字符串转换成指定格式的date
	 *
	 * @param 日期字符串
	 * @param 日期格式
	 * @return
	 */
	public static Date formatStr(String str, String formatStr) {

		Date date = null;
		if (str != null) {
			DateFormat sdf = new SimpleDateFormat(formatStr);
			try {
				date = sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * 得到本月的第一天，格式：yyyy-MM-dd hh:mm:ss
	 *
	 * @return
	 */
	public static String getMonthFirstDay() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到指定时间月份的第一天(0:0:0)
	 */
	public static Date getMonthFirstDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
		return calendar.getTime();

	}

	/**
	 * 得到下个月的第一天
	 */
	public static String getNextMonthFirstDay() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到本月的最后一天
	 *
	 * @return
	 */
	public static String getMonthLastDay() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到批定月的最后一天(23:59:59)
	 *
	 * @param date
	 * @return
	 */
	public static Date getMonthLastDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
		return calendar.getTime();
	}

	/**
	 * 得到本周的第一天
	 */
	public static String getWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, 2);

		// 获取本周第一天的日期
		String weekBegin = formatDate(calendar.getTime(), DATE_FMT);
		// 获得当前日期的日期
		String nowDate = formatDate(Calendar.getInstance().getTime(), DATE_FMT);

		// 如果本周第一天日期大于当前日期，那么将日期减6天返回
		if (parse(DATE_FMT, weekBegin).getTime() > parse(DATE_FMT, nowDate).getTime()) {
			return getNDay(-6);
		}
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到n个星期前
	 *
	 * @param n
	 * @return
	 */
	public static String getLastestWeek(int n) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - n);
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到n个月前
	 *
	 * @param n
	 * @return
	 */
	public static String getLastestMonth(int n) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONTH) - n);
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 根据制定日期获取当天时间
	 *
	 * @return
	 */
	public static String getDay(String formatStr) {

		Date date = new Date();
		return DateUtil.formatDate(date, formatStr);
	}

	/**
	 * 获取第二天时间
	 *
	 * @return
	 */
	public static String getTomorrow() {

		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) + 1);
		return DateUtil.formatDate(now.getTime(), DATE_FMT);
	}

	/**
	 * 获取N天前后的时间
	 *
	 * @param n
	 * @return
	 */
	public static String getNDay(int n) {

		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) + n);
		return DateUtil.formatDate(now.getTime(), DATE_FMT);
	}

	/**
	 * 获取昨日的时间
	 *
	 * @param n
	 * @return
	 */
	public static String getYesterDay() {

		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) - 1);
		return DateUtil.formatDate(now.getTime(), DATE_FMT);
	}

	/**
	 * 获取n个月以后的时间字符串
	 *
	 * @param day
	 * @param n
	 * @return
	 */
	public static Date getDateAfterSeveralMonth(Date day, int n) {

		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) + n);
		return c.getTime();
	}

	/**
	 * 获取n个 月以后的时间(毫秒)
	 *
	 * @param day
	 * @param n
	 * @return
	 */
	public static long getDateTimeAfterSeveralMonth(Date day, int n) {

		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) + n);
		return c.getTimeInMillis();
	}

	/**
	 * 时间比较返回天数
	 *
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {

		if ((null == fDate) || (null == oDate)) {

			return -1;
		}
		long intervalMilli = oDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}

	public static int getIntervalMinutes(Date fDate, Date oDate) {

		if ((null == fDate) || (null == oDate)) {
			return -1;
		}
		long intervalMilli = oDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (1000 * 60));

	}

	public static int getIntervalSeconds(Date fDate, Date oDate) {

		if ((null == fDate) || (null == oDate)) {
			return -1;
		}
		long intervalMilli = oDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (1000));

	}

	/**
	 * 时间比较返回天数
	 *
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {

		Calendar aCalendar = Calendar.getInstance();

		aCalendar.setTime(fDate);

		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

		aCalendar.setTime(oDate);

		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

		return day2 - day1;

	}

	public static long sceondOfTwoDate(Date createtime, int minuttes) {

		Date datenow = new Date();
		long diff = ((createtime.getTime() + (minuttes * 60 * 1000)) - datenow.getTime()) / 1000;
		if (diff <= 0) {
			diff = 0;
		}
		return diff;
	}

	public static Date getNextDay(Date sDate) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(sDate);

		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);

		return calendar.getTime();
	}

	/**
	 * 得到本月的第一天
	 *
	 * @return
	 */
	public static Date getFirstDayOfCurrentMonth() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * 返回当前月的最后一天最后一秒
	 *
	 * @return
	 */
	public static Date getLastTimeOfLastDayOfCurrentMonth() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
		return calendar.getTime();
	}

	/**
	 * 返回指定天的最后一秒的时间
	 *
	 * @param sDate
	 * @return
	 */
	public static Date getLastestTimeOfDay(Date sDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
		return calendar.getTime();
	}

	public static String skipDateTime(String timeStr, int skipDay) {

		String pattern = "yyyy-MM-dd HH:mm";
		Date d = parse(pattern, timeStr);
		Date date = skipDateTime(d, skipDay);
		return formatDateTime(pattern, date);
	}

	public static java.util.Date parse(String pattern, String strDateTime) {

		java.util.Date date = null;
		if ((strDateTime == null) || (pattern == null)) {
			return null;
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			formatter.setLenient(false);
			date = formatter.parse(strDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String formatDateTime(String pattern, java.util.Date date) {

		String strDate = null;
		String strFormat = pattern;
		SimpleDateFormat dateFormat = null;

		if (date == null) {
			return "";
		}
		dateFormat = new SimpleDateFormat(strFormat);
		strDate = dateFormat.format(date);

		return strDate;
	}

	public static Date skipDateTime(Date d, int skipDay) {

		if (d == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, skipDay);
		return calendar.getTime();
	}

	/**
	 * 给指定的时间加上指定的天数小时数、分钟数和秒数后返回
	 */
	public static Date getDateTime(Date date, int skipDay, int skipHour, int skipMinute, int skipSecond) {

		if (null == date) {
			return null;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_MONTH, skipDay);
		cal.add(Calendar.HOUR_OF_DAY, skipHour);
		cal.add(Calendar.MINUTE, skipMinute);
		cal.add(Calendar.SECOND, skipSecond);

		return cal.getTime();
	}

	/**
	 * 根据生日（yyyy-MM-dd）获取年龄
	 *
	 * @param 生日字符串
	 * @return
	 */
	public static int getAge(String birthday) {

		if (StringUtil.isBlank(birthday)) {
			return -1;
		}
		Date d1 = DateUtil.formatStr(birthday, DATE_FMT);
		long times = (new Date().getTime()) - d1.getTime();
		int age = Long.valueOf((times / (1000 * 60 * 60 * 24)) / 365).intValue();
		return age > 0 ? age : 1;
	}

	/**
	 * 从身份证号码中获取生日日期
	 *
	 * @param cardID
	 * @return
	 */
	public static Date getBirthday(String cardID) {

		if (StringUtil.isBlank(cardID)) {
			return null;
		}

		if (cardID.length() == 15) {
			Pattern p = Pattern.compile(
					"^[1-9]\\d{5}(\\d{2}((((0[13578])|(1[02]))(([0-2][0-9])|(3[01])))|(((0[469])|(11))(([0-2][0-9])|(30)))|(02[0-2][0-9])))\\d{3}$");
			Matcher m = p.matcher(cardID);
			if (m.matches()) {
				String s = m.group(1);
				try {
					return new SimpleDateFormat("yyyyMMdd").parse("19" + s);
				} catch (ParseException e) {
				}
			}
		} else if (cardID.length() == 18) {
			Pattern p = Pattern.compile(
					"^[1-9]\\d{5}(\\d{4}((((0[13578])|(1[02]))(([0-2][0-9])|(3[01])))|(((0[469])|(11))(([0-2][0-9])|(30)))|(02[0-2][0-9])))\\d{3}[\\dX]$");
			Matcher m = p.matcher(cardID);
			if (m.matches()) {
				String s = m.group(1);
				try {
					return new SimpleDateFormat("yyyyMMdd").parse(s);
				} catch (ParseException e) {
				}
			}
		}

		return null;
	}

}
