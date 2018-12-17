package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.entity.Holiday;

import java.util.Date;
import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/11
 **/
public interface HolidayService {
    //add holidayƒ
    void addHolidays(String datas);

    // TODO: 2018/12/11 other api
    List<Holiday> getHolidays(String from, String to);

    void deleteHoliday(String date);
}
