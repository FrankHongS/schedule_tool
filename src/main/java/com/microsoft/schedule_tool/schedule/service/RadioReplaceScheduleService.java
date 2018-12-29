package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.vo.response.RespReplaceSchedule;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/18
 **/
public interface RadioReplaceScheduleService {
    //add
    long addReplace(long roleId, String date, long employeeId);

    //delete
    void deleteReplace(long replaceId);

    //getAll
    List<RespReplaceSchedule> getAllReplace(String from, String to);

    List<RespReplaceSchedule> getAllReplace();

    //add some
    void addSomeReplace(long roleId, String date, long employeeId);

}
