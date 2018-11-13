package com.microsoft.schedule_tool.vo.leavesum;

import java.util.List;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 */
public class LeaveYearSum {

    private String name;

    private List<Integer> allLeaveYearSum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getAllLeaveYearSum() {
        return allLeaveYearSum;
    }

    public void setAllLeaveYearSum(List<Integer> allLeaveYearSum) {
        this.allLeaveYearSum = allLeaveYearSum;
    }
}
