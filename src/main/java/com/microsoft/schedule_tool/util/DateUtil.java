package com.microsoft.schedule_tool.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Frank Hon on 11/9/2018
 * E-mail: v-shhong@microsoft.com
 */
public class DateUtil {

    public static Date parseDateString(String dateString) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }

    public static String parseDateToString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String parseMonthString(String monthStr){
        String[] strArray=monthStr.split("-");
        String year=strArray[0];
        String month=strArray[1];
        int nextMonth=Integer.valueOf(month)+1;
        return year+"-"+nextMonth+"-01";
    }

    public static int getDayCountFromDate(String small,String big) throws ParseException {
        Date smallDate=parseDateString(small);
        Date bigDate=parseDateString(big);
        long time=bigDate.getTime()-smallDate.getTime();
        return  (int) (time/(24*60*60*1000));
    }
}
