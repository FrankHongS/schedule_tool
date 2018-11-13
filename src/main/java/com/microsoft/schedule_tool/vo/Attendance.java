package com.microsoft.schedule_tool.vo;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public class Attendance {

    private int employeeId;

    private String name;

    private String alias;

    private Float annualCount;

    private int leaveSum;

    private int lateSum;

    public Attendance(){}

    public Attendance(int employeeId,String name, String alias, float annualCount, int leaveSum, int lateSum) {
        this.employeeId=employeeId;
        this.name = name;
        this.alias = alias;
        this.annualCount=annualCount;
        this.leaveSum = leaveSum;
        this.lateSum = lateSum;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Float getAnnualCount() {
        return annualCount;
    }

    public void setAnnualCount(Float annualCount) {
        this.annualCount = annualCount;
    }

    public int getLeaveSum() {
        return leaveSum;
    }

    public void setLeaveSum(int leaveSum) {
        this.leaveSum = leaveSum;
    }

    public int getLateSum() {
        return lateSum;
    }

    public void setLateSum(int lateSum) {
        this.lateSum = lateSum;
    }

}
