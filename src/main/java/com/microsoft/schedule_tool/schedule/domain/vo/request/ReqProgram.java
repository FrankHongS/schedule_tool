package com.microsoft.schedule_tool.schedule.domain.vo.request;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
public class ReqProgram {
    public long radioStationId;
    public String programName;

    public ReqProgram() {
    }

    public ReqProgram(long radioStationId, String programName) {
        this.radioStationId = radioStationId;
        this.programName = programName;
    }
}
