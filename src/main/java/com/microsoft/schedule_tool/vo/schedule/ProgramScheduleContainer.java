package com.microsoft.schedule_tool.vo.schedule;

import java.util.List;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ProgramScheduleContainer {

    private String from;

    private String to;

    private List<ProgramSchedule> programSchedules;

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

    public List<ProgramSchedule> getProgramSchedules() {
        return programSchedules;
    }

    public void setProgramSchedules(List<ProgramSchedule> programSchedules) {
        this.programSchedules = programSchedules;
    }
}
