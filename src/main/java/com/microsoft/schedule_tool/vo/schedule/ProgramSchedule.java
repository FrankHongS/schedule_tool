package com.microsoft.schedule_tool.vo.schedule;

import java.util.List;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ProgramSchedule {

    private String programName;

    private List<String> employeeList;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public List<String> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<String> employeeList) {
        this.employeeList = employeeList;
    }
}
