package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.vo.Attendance;
import com.microsoft.schedule_tool.vo.Pager;

import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */

public interface SumService {

    Pager<Attendance> getSumOfAllTypes();

    Pager<Attendance> getSumOfAllTypesByAlias(String alias);

    Pager<Attendance> getSumByPage(Integer page,Integer size);

    Pager<Attendance> getAllSumByDateRangeByPage(Integer page, Integer size, String from, String to);

    Pager<Attendance> getSumByDateRangeAndAlias(Integer page, Integer size, String from, String to, String alias);
}
