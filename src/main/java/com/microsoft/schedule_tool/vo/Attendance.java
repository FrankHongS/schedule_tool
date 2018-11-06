package com.microsoft.schedule_tool.vo;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public class Attendance {

    private String name;

    private String alias;

    private int leaveSum;

    private int lateSum;

    private int homebaseSum;

    public Attendance(){}

    public Attendance(String name, String alias, int leaveSum, int lateSum, int homebaseSum) {
        this.name = name;
        this.alias = alias;
        this.leaveSum = leaveSum;
        this.lateSum = lateSum;
        this.homebaseSum = homebaseSum;
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

    public int getHomebaseSum() {
        return homebaseSum;
    }

    public void setHomebaseSum(int homebaseSum) {
        this.homebaseSum = homebaseSum;
    }
}
