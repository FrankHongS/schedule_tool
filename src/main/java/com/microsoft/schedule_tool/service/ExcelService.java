package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.vo.excel.MonthDetailSum;
import com.microsoft.schedule_tool.vo.excel.YearSum;

import java.util.List;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ExcelService {

    List<YearSum> getYearSum(String year);

    List<MonthDetailSum> getMonthDetailSum(String month);

}
