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

    private List<Integer> yearSum;

    private List<List<Integer>> monthSum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getYearSum() {
        return yearSum;
    }

    public void setYearSum(List<Integer> yearSum) {
        this.yearSum = yearSum;
    }

    public List<List<Integer>> getMonthSum() {
        return monthSum;
    }

    public void setMonthSum(List<List<Integer>> monthSum) {
        this.monthSum = monthSum;
    }

}