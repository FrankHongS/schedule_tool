package com.microsoft.schedule_tool.vo.leavesum;

import java.util.List;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 *
 * sum of both leave and late
 */
public class YearSum {

    private String name;

    private List<Float> yearSum;

    private List<List<Float>> monthSum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Float> getYearSum() {
        return yearSum;
    }

    public void setYearSum(List<Float> yearSum) {
        this.yearSum = yearSum;
    }

    public List<List<Float>> getMonthSum() {
        return monthSum;
    }

    public void setMonthSum(List<List<Float>> monthSum) {
        this.monthSum = monthSum;
    }

}