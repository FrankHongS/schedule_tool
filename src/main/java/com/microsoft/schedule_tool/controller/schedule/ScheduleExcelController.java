package com.microsoft.schedule_tool.controller.schedule;

import com.microsoft.schedule_tool.service.schedule.ScheduleExcelService;
import com.microsoft.schedule_tool.util.schedule.ExportScheduleUtil;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/schedule_excel")
public class ScheduleExcelController {

    @Autowired
    private ScheduleExcelService mScheduleExcelService;

    @GetMapping("/table")
    public void exportScheduleTable(HttpServletResponse response, @RequestParam("from") String from, @RequestParam("to") String to) throws IOException, ParseException {
        String fileName=System.currentTimeMillis()+".xlsx";
        ProgramScheduleContainer container=mScheduleExcelService.getProgramSchedule(from, to);

        ExportScheduleUtil.exportScheduleTable(response,fileName,container);
    }
}