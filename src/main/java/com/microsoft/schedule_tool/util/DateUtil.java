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
}
