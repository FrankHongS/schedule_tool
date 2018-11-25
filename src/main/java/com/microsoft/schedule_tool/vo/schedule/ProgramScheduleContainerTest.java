package com.microsoft.schedule_tool.vo.schedule;

import java.util.List;

/**
 * Created by Frank Hon on 11/23/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ProgramScheduleContainerTest {

    private String from;

    private String to;

    private List<ProgramScheduleTest> programScheduleList;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<ProgramScheduleTest> getProgramScheduleList() {
        return programScheduleList;
    }

    public void setProgramScheduleList(List<ProgramScheduleTest> programScheduleList) {
        this.programScheduleList = programScheduleList;
    }
}
