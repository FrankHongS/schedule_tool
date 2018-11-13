package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.service.ExcelService;
import com.microsoft.schedule_tool.util.ExcelUtil;
import com.microsoft.schedule_tool.vo.leavesum.LeaveYearSum;
import com.microsoft.schedule_tool.vo.leavesum.YearSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService mExcelService;

    @GetMapping("/export")
    public void excel(HttpServletResponse response,@RequestParam("year") String year) throws IOException {

        List<YearSum> yearSumList=mExcelService.getYearSum(year);

        String filename=System.currentTimeMillis()+".xlsx";
        ExcelUtil.exportExcel(response,filename, yearSumList);
    }
}
