package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.vo.Attendance;

import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */

public interface SumService {

    List<Attendance> getSumOfAllTypes();

    List<Attendance> getSumOfAllTypesByAlias(String alias);

    List<Attendance> getAllSumByDateRange(String from, String to);

    List<Attendance> getSumByDateRangeAndAlias(String from, String to, String alias);
}
