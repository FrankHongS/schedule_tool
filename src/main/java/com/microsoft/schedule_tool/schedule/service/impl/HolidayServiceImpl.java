package com.microsoft.schedule_tool.schedule.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.Holiday;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqHoliday;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqRole;
import com.microsoft.schedule_tool.schedule.repository.HolidayRepository;
import com.microsoft.schedule_tool.schedule.service.HolidayService;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
        try {
            for (int i = 0; i < holidays.size(); i++) {
                ReqHoliday reqHoliday = holidays.get(i);
                Holiday holiday = new Holiday();
                Date date = DateUtil.parseDateString(reqHoliday.date);
                holiday.setDate(date);
                //查重
                if (canAdd(date)) {
                    holidayRepository.save(holiday);
                }
            }
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.HOLIDAY_ADD_ERROR);
        }

    }

    @Override
    public List<Holiday> getHolidays(String from, String to) {
        try {
            Date fromDate = DateUtil.parseDateString(from);
            Date toDate = DateUtil.parseDateString(to);
            List<Holiday> holidays = holidayRepository.getAllByDateLessThanEqualAndDateGreaterThanEqual(toDate, fromDate);
            return holidays;
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.HOLIDAY_FIND_FAILED);
        }
    }

    @Override
    public void deleteHoliday(String date) {
        try {
            Date date1 = DateUtil.parseDateString(date);
            holidayRepository.deleteHolidayByDate(date1);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.HOLIDAY_DELETE_FAILED);
        }
    }

    private boolean canAdd(Date date) {
        List<Holiday> all = holidayRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getDate().equals(date)) {
                return false;
            }
        }
        return true;
    }
}
