package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.service.ExcelService;
import com.microsoft.schedule_tool.util.excel.ExportMonthDetailSumUtil;
import com.microsoft.schedule_tool.util.excel.ExportYearSumUtil;
import com.microsoft.schedule_tool.vo.excel.MonthDetailSum;
import com.microsoft.schedule_tool.vo.excel.YearSum;
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
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService mExcelService;

    @GetMapping("/export_year")
    public void exportYearSum(HttpServletResponse response,@RequestParam("year") String year) throws IOException {

        List<YearSum> yearSumList=mExcelService.getYearSum(year);

        String filename=year+".xlsx";
        ExportYearSumUtil.exportYearSumExcel(response,filename, yearSumList);
    }

    @GetMapping("/export_month")
    public void exportMonthSum(HttpServletResponse response,@RequestParam("month") String month) throws IOException, ParseException {

        List<MonthDetailSum> monthDetailSumList=mExcelService.getMonthDetailSum(month);

        String filename=month+".xlsx";
        ExportMonthDetailSumUtil.exportMonthDetailSum(response,filename,monthDetailSumList);
    }
}
