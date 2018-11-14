package com.microsoft.schedule_tool.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/14/2018
 * E-mail: v-shhong@microsoft.com
 */
public class MonthDetailSum {

    private String name;

    private String month;

    private Map<Long,String> descMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Map<Long, String> getDescMap() {
        return descMap;
    }

    public void setDescMap(Map<Long, String> descMap) {
        this.descMap = descMap;
    }
}
