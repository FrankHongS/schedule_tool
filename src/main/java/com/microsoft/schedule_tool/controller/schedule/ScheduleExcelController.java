package com.microsoft.schedule_tool.controller.schedule;

import com.microsoft.schedule_tool.entity.schedule.Substitute;
import com.microsoft.schedule_tool.service.schedule.ScheduleExcelService;
import com.microsoft.schedule_tool.service.schedule.SubstituteService;
import com.microsoft.schedule_tool.util.schedule.ExportScheduleUtil;
import com.microsoft.schedule_tool.util.schedule.ExportScheduleUtilTest;
import com.microsoft.schedule_tool.util.schedule.ExportSubstituteUtil;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainer;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/schedule_excel")
public class ScheduleExcelController {

    @Autowired
    private ScheduleExcelService mScheduleExcelService;

    @Autowired
    private SubstituteService mSubstituteService;

    @GetMapping("/table")
    public void exportScheduleTable(HttpServletResponse response, @RequestParam("from") String from, @RequestParam("to") String to) throws IOException, ParseException {
        String fileName=System.currentTimeMillis()+".xlsx";
//        ProgramScheduleContainer container=mScheduleExcelService.getProgramSchedule(from, to);
//
//        ExportScheduleUtil.exportScheduleTable(response,fileName,container);

        ProgramScheduleContainerTest container=mScheduleExcelService.getProgramScheduleTest(from,to);

        ExportScheduleUtilTest.exportScheduleTable(response,fileName,container);
    }

    @GetMapping("/substitute")
    public void exportSubstitute(HttpServletResponse response, @RequestParam("isHoliday") Boolean isHoliday,
                                 @RequestParam(name = "from",required = false) String from, @RequestParam(name = "to",required = false) String to) throws IOException, ParseException {

        List<Substitute> substituteList;

        String fileName;
        String hol=isHoliday?"节假日":"平时";
        fileName=hol+"替班表.xlsx";

        if(from==null||to==null){
            substituteList=mSubstituteService.getAllHolidayOrNotHolidaySubstitutes(isHoliday);
        }else{
            substituteList=mSubstituteService.getAllSubstitutesInDataRange(isHoliday, from, to);
        }

        ExportSubstituteUtil.exportScheduleTable(response,fileName,substituteList);

    }
}
