package com.microsoft.schedule_tool.schedule.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.schedule.domain.entity.Holiday;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqHoliday;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqRole;
import com.microsoft.schedule_tool.schedule.repository.HolidayRepository;
import com.microsoft.schedule_tool.schedule.service.HolidayService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/11
 **/
@Service
public class HolidayServiceImpl implements HolidayService {
    @Autowired
    private HolidayRepository holidayRepository;

    @Override
    public void addHolidays(String datas) {
        List<ReqHoliday> holidays = new ArrayList<>();
        try {
            ObjectMapper om = new ObjectMapper();
            JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, ReqHoliday.class);
            holidays = om.readValue(datas, javaType);
            if (holidays.size() <= 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.HOLIDAY_PARAMS_ERROR);
        }
        for (int i = 0; i < holidays.size(); i++) {
            ReqHoliday reqHoliday = holidays.get(i);
            Holiday holiday = new Holiday();
            holiday.setDate(reqHoliday.date);
            holidayRepository.save(holiday);
        }

    }
}
