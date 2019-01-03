package com.microsoft.schedule_tool.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Frank Hon on 11/9/2018
 * E-mail: v-shhong@microsoft.com
 */
public class DateUtil {

    public static Date parseDateString(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }

    public static String parseDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String parseMonthString(String monthStr) {
        String[] strArray = monthStr.split("-");
        String year = strArray[0];
        String month = strArray[1];
        int nextMonth = Integer.valueOf(month) + 1;
        return year + "-" + nextMonth + "-01";
    }

    public static int getDayCountFromDate(String small, String big) throws ParseException {
        Date smallDate = parseDateString(small);
        Date bigDate = parseDateString(big);
        long time = bigDate.getTime() - smallDate.getTime();
        return (int) (time / (24 * 60 * 60 * 1000));
    }

    /**
     * @param dateStr date string
     * @return week of year and day of week
     * @throws ParseException
     */
    public static int getWeekOfYear(String dateStr) throws ParseException {
        Date date = parseDateString(dateStr);

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getDayOfWeek(String dateStr) throws ParseException {
        Date date = parseDateString(dateStr);

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
        } else {
            day -= 1;
        }

        return day;
    }

    // 获取某年的第几周的开始日期
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        Calendar cal = (Calendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    // 获取当前时间所在周的开始日期
    private static Date getFirstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }


    public static Date getNextDate(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, interval);
        return calendar.getTime();
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);//获取年份
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }


    static long until(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public static long weeks(Date date, Date date1) {
        Date thisWeekMonday = getThisWeekMonday(date);
        Date thisWeekMonday1 = getThisWeekMonday(date1);
        LocalDate start = date2localDate(thisWeekMonday);
        LocalDate end = date2localDate(thisWeekMonday1);

        long until = until(start, end);
        return until / 7 + 1;
    }

    private static LocalDate date2localDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate start = instant.atZone(zoneId).toLocalDate();
        return start;
    }

    public static boolean isSameDay(Date date, Date date1) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);

        return (c.get(Calendar.YEAR) == c1.get(Calendar.YEAR) &&
                c.get(Calendar.MONTH) == c1.get(Calendar.MONTH) &&
                c.get(Calendar.DAY_OF_MONTH) == c1.get(Calendar.DAY_OF_MONTH));
    }

    public static int getBetweenWeeks(Date date1, Date date2) {
        //1:获取date1的星期一的日期temp  2：求date2跟temp之前的天数count  3：return count/7
        Date temp = getThisWeekMonday(date1);

        long time = date2.getTime() - temp.getTime();
        int i = (int) (time / (24 * 60 * 60 * 1000) + 0.5f);
        return i / 7;
    }
}
