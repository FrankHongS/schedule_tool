package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespSchedule;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.util.schedule.ExportExcelUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author kb_jay
 * @time 2018/12/17
 **/
@RestController
@RequestMapping("/schedule")
public class RadioScheduleController {
    @Autowired
    private ScheduleSercive scheduleSercive;

    /**
     * 添加假期人员
     *
     * @param date
     * @param roleId
     * @param employeeId
     * @return
     */
    @PostMapping("/add_holiday")
    public Result addHolidayEmployee(String date, long roleId, long employeeId) {
        scheduleSercive.addHolidayEmployee(date, roleId, employeeId);
        return ResultUtil.success();
    }

    @GetMapping("/delete_holiday")
    public Result deleteHoliday(long id) {
        scheduleSercive.deleteHolidaySchedule(id);
        return ResultUtil.success();
    }



    @GetMapping()
    public Result getSchdules(String from, String to, boolean isHoliday) {
        List<RespSchedule> allSchedule = scheduleSercive.getAllSchedule(from, to, isHoliday);
        Map<String, List<RespSchedule>> data = ResultUtil.getResultData("data", allSchedule);
        return ResultUtil.success(data);
    }

}
