package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioReplaceSchedule;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespReplaceSchedule;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespSchedule;
import com.microsoft.schedule_tool.schedule.service.RadioReplaceScheduleService;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.util.schedule.ExportExcelUtil;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/18
 **/
@Controller
@RequestMapping("/excel")
public class ExportExcelController {

    @Autowired
    private RadioReplaceScheduleService radioReplaceScheduleService;

    @Autowired
    private ScheduleSercive scheduleSercive;

    @GetMapping("/export_schedule")
    public void exportSchedule(HttpServletResponse response, @RequestParam("from") String from,
                               @RequestParam("to") String to,
                               @RequestParam("isHoliday") boolean isHoliday) {
        List<RespSchedule> allSchedule = scheduleSercive.getAllSchedule(from, to, isHoliday);
        try {
            String fileName = System.currentTimeMillis() + ".xlsx";
            if (isHoliday) {
                ExportExcelUtil.exportHolidayScheduleTable(response, from, to, fileName, allSchedule);
            } else {
                ExportExcelUtil.exportScheduleTable(response, from, to, fileName, allSchedule);
            }
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_EXPORT_FAILED);
        }
    }

    @GetMapping("/export_replace")
    public void exportReplaceSchedule(HttpServletResponse response, @RequestParam("from") String from,
                                      @RequestParam("to") String to
    ) {
        List<RespReplaceSchedule> allReplace = radioReplaceScheduleService.getAllReplace(from, to);

        try {
            String fileName = System.currentTimeMillis() + ".xlsx";
            ExportExcelUtil.exportReplaceScheduleTable(response, fileName, from, to, allReplace);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_EXPORT_FAILED);
        }

    }
}
