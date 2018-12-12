package com.microsoft.schedule_tool.util;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
public class StringUtils {
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }
}
