package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.vo.response.RespSchedule;

import java.util.Date;
import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/12
 **/
public interface ScheduleSercive {
    void schedule(String from, String to);

    void addHolidayEmployee(String date, long roleId, long empoyeeId);

    void deleteHolidaySchedule(long id);

    List<RespSchedule> getAllSchedule(String from, String to, boolean isHoliday);

    List<RespSchedule> getHolidaySchedule();
}
