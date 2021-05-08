package com.cdk8s.code.gen.util;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 */
public final class DatetimeUtil {


	private static final String STRING_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat SDF_YY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SDF_YY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");


	//=====================================JDK8 包 start=====================================

	public static long currentEpochMilli() {
		return Instant.now().toEpochMilli();
	}

	public static long currentEpochSecond() {
		return Instant.now().getEpochSecond();
	}

	public static LocalDateTime getNowLocalDateTime() {
		return LocalDateTime.now();
	}

	public static Instant getNowInstant() {
		return Instant.now();
	}

	public static long toEpochMilli(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static long toEpochMilli(String str) {
		return toLocalDateTime(str).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static String currentDateTime() {
		return toDatetimeString(toLocalDateTime(currentEpochMilli()), null);
	}

	public static String currentDateTime(String pattern) {
		return toDatetimeString(toLocalDateTime(currentEpochMilli()), pattern);
	}

	public static String toDatetimeString(LocalDateTime localDateTime, String pattern) {
		if (StringUtil.isBlank(pattern)) {
			pattern = STRING_PATTERN;
		}
		return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
	}

	public static String toDatetimeString(long epochMill) {
		return toDatetimeString(toLocalDateTime(epochMill), null);
	}

	public static String toDatetimeString(long epochMill, String pattern) {
		return toDatetimeString(toLocalDateTime(epochMill), pattern);
	}

	public static LocalDateTime toLocalDateTime(String str) {
		return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(STRING_PATTERN));
	}

	public static LocalDateTime toLocalDateTime(long epochMill) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMill), ZoneId.systemDefault());
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date toDate(Long epochMilli) {
		return new Date(epochMilli);
	}

	/**
	 * time1 如果在 time2 之前的日期，则返回 true
	 *
	 * @param time1，2018-04-23 23:24:04
	 * @param time2，2018-04-27 23:24:04
	 * @return
	 */
	public static boolean isBefore(LocalDateTime time1, LocalDateTime time2) {
		return time1.isBefore(time2);
	}

	/**
	 * time1 如果在 time2 之后的日期，则返回 true
	 *
	 * @param time1，2018-04-27 23:24:04
	 * @param time2，2018-04-23 23:24:04
	 * @return
	 */
	public static boolean isAfter(LocalDateTime time1, LocalDateTime time2) {
		return time2.isBefore(time1);
	}

	/**
	 * 如果 time1 和 time2 在日期上是一样的，则返回 true
	 *
	 * @param time1，2018-04-27 23:24:04
	 * @param time2，2018-04-27 23:24:04
	 * @return
	 */
	public static boolean isEqual(LocalDateTime time1, LocalDateTime time2) {
		return time1.isEqual(time2);
	}

	/**
	 * 比较时间差值
	 * 如果 time1 早于 time2 结果是正数：96
	 * time1 = 2018-04-22 23:24:04
	 * time2 = 2018-04-26 23:24:04
	 * 如果 time1 晚于 time2 结果是负数：-96
	 * time1 = 2018-04-26 23:24:04
	 * time2 = 2018-04-22 23:24:04
	 *
	 * @param time1
	 * @param time2
	 * @param chronoUnit 有：ChronoUnit.MILLIS ChronoUnit.SECONDS ChronoUnit.MINUTES ChronoUnit.HOURS ChronoUnit.DAYS 等等
	 * @return
	 */
	public static long between(LocalDateTime time1, LocalDateTime time2, ChronoUnit chronoUnit) {
		return chronoUnit.between(time1, time2);
	}

	public static long between(Long beginTime, Long endTime, ChronoUnit chronoUnit) {
		return between(toLocalDateTime(beginTime), toLocalDateTime(endTime), chronoUnit);
	}

	public static long betweenNowAndFutureTime(LocalDateTime time2, ChronoUnit chronoUnit) {
		LocalDateTime time1 = getNowLocalDateTime();
		return between(time1, time2, chronoUnit);
	}

	public static long betweenNowAndPastTime(LocalDateTime time1, ChronoUnit chronoUnit) {
		LocalDateTime time2 = getNowLocalDateTime();
		return between(time1, time2, chronoUnit);
	}

	/**
	 * 获取指定时间当天的开始时间
	 */
	public static LocalDateTime getLocalDateTimeStart(Long epochMilli) {
		if (epochMilli == null) {
			return null;
		}

		LocalDateTime localDateTime = toLocalDateTime(epochMilli);
		LocalDateTime localDateTimeStart = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), 0, 0, 0);
		return localDateTimeStart;

	}

	/**
	 * 获取指定时间当天的结束时间
	 */
	public static LocalDateTime getLocalDateTimeEnd(Long epochMilli) {
		if (epochMilli == null) {
			return null;
		}

		LocalDateTime localDateTime = toLocalDateTime(epochMilli);
		LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), 23, 59, 59);
		return localDateTimeEnd;
	}

	/**
	 * 获取指定时间当天的开始时间戳
	 */
	public static Long getEpochMilliStart(Long epochMilli) {
		if (epochMilli == null) {
			return null;
		}

		return toEpochMilli(getLocalDateTimeStart(epochMilli));
	}

	/**
	 * 获取指定时间当天的结束时间戳
	 */
	public static Long getEpochMilliEnd(Long epochMilli) {
		if (epochMilli == null) {
			return null;
		}

		return toEpochMilli(getLocalDateTimeEnd(epochMilli));
	}


	//=====================================JDK8 包  end=====================================

	//=====================================其他包 start=====================================


	public static String getYesterday() {
		return getYesterday("yyyy-MM-dd");
	}

	public static String getYesterday(String pattern) {
		DateTime yesterday = DateUtil.yesterday();
		return yesterday.toString(pattern);
	}

	public static String formatDate(final Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(final Date date, Object... pattern) {
		String formatDate;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}


	public static String formatDateTime(final Date date) {
		return formatDateTime(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDateTime(final Date date, Object... pattern) {
		return formatDate(date, pattern);
	}


	/**
	 * 以当前时间为路径（到小时）
	 *
	 * @return
	 */
	public static String getCurrentDateTimeByFilePath() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("yyyy");
		stringBuilder.append(File.separator);
		stringBuilder.append("MM");
		stringBuilder.append(File.separator);
		stringBuilder.append("dd");
		stringBuilder.append(File.separator);
		stringBuilder.append("HH");
		return formatDate(new Date(), stringBuilder.toString());
	}


	/**
	 * 计算时间加减(可以计算:分钟,小时,天数,月份)
	 *
	 * @param assignDate    指定日期
	 * @param intervalValue 如果未负数表示:减去指定(正数表示加)
	 * @param dateCodeType  DATE计算天数,HOUR计算小时,MINUTE计算分钟,MONTH计算月份,YEAR计算年份
	 * @return 用法eg:DateUtils.dateAndIntervalValue(new Date(), -1, "DATE");
	 */
	public static String dateAndIntervalValue(final Date assignDate, final int intervalValue, final String dateCodeType, String pattern) {

		//把Date转换成Calendar
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(assignDate);

		if (dateCodeType.equals("DATE")) {
			calendar.add(Calendar.DATE, intervalValue);//intervalValue如果未负数表示:减去指定天数(正数表示加)
		} else if (dateCodeType.equals("HOUR")) {
			calendar.add(Calendar.HOUR, intervalValue);//intervalValue如果未负数表示:减去指定小时(正数表示加)
		} else if (dateCodeType.equals("MINUTE")) {
			calendar.add(Calendar.MINUTE, intervalValue);//intervalValue如果未负数表示:减去指定分钟(正数表示加)
		} else if (dateCodeType.equals("MONTH")) {
			calendar.add(Calendar.MONTH, intervalValue);//intervalValue如果未负数表示:减去指定个月(正数表示加)
		} else if (dateCodeType.equals("YEAR")) {
			calendar.add(Calendar.YEAR, intervalValue);//intervalValue如果未负数表示:减去指定个年(正数表示加)
		}

		Date dateResult = calendar.getTime();
		if (StringUtil.isBlank(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}

		return formatDate(dateResult, pattern);

	}

	/**
	 * 返回相差时间的毫秒值转换为人类可读格式
	 *
	 * @param betweenTime 相差时间的毫秒数
	 * @param level       返回格式的等级，返回 MINUTE 则表示读取分钟，如果是 HOUR 则返回到小时
	 * @return 返回格式：69天10小时40分
	 */
	public static String longTimeString(Long betweenTime, BetweenFormater.Level level) {
		if (null == level) {
			level = BetweenFormater.Level.MILLSECOND;
		}
		BetweenFormater betweenFormater = new BetweenFormater(betweenTime, level);
		return betweenFormater.format();
	}

	/**
	 * 获取指定时间属于当月的第几周（这里周一才是一周的第一天（默认是周天））
	 * 2020-03-01 返回 1
	 */
	public static int getWeekOfMonth(Date date) {
		return DateUtil.weekOfMonth(date);
	}

	/**
	 * 获取月份，从0开始计数，0代表1月
	 *
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		return DateUtil.month(date);
	}

	/**
	 * 获取一周开始到结束的list集合
	 *
	 * @param beginString
	 * @param endString
	 * @return
	 */
	@SneakyThrows
	public static List<Date> findDates(String beginString, String endString) {
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dBegin = sdf.parse(beginString);
		Date dEnd = sdf.parse(endString);
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	/**
	 * 获取一周开始到结束的list集合
	 *
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	/**
	 * 根据日期获得所在周的日期区间（周一和周日日期）
	 *
	 * @param date 比如这里传的是：2020-03-01 则得到
	 * @return 格式：2020-02-24,2020-03-01
	 */
	public static String getTimeInterval(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String imptimeBegin = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf.format(cal.getTime());
		return imptimeBegin + "," + imptimeEnd;
	}

	/**
	 * 根据当前日期获得上周的日期区间（上周周一和周日日期）
	 * 比如当前时间：2020-10-27
	 *
	 * @return 格式：2020-10-19,2020-10-25
	 */
	public static String getLastTimeInterval() {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		int offset1 = 1 - dayOfWeek;
		int offset2 = 7 - dayOfWeek;
		calendar1.add(Calendar.DATE, offset1 - 7);
		calendar2.add(Calendar.DATE, offset2 - 7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastBeginDate = sdf.format(calendar1.getTime());
		String lastEndDate = sdf.format(calendar2.getTime());
		return lastBeginDate + "," + lastEndDate;
	}

	//=====================================其他包  end=====================================


	//=====================================私有方法 start=====================================

	//=====================================私有方法  end=====================================


}



