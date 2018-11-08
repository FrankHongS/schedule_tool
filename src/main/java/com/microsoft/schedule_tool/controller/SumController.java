package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.service.SumService;
import com.microsoft.schedule_tool.vo.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/sum")
public class SumController {

    private static final String KEY="sum";

    @Autowired
    private SumService mSumService;

    @GetMapping
    public Map<String, List<Attendance>> getAttendanceList(){
        Map<String, List<Attendance>> resultMap=new HashMap<>();

        List<Attendance> attendanceList=mSumService.getSumOfAllTypes();

        resultMap.put(KEY,attendanceList);

        return resultMap;
    }

    @GetMapping("/alias")
    public Map<String, List<Attendance>> getAttendanceListByAlias(
            @RequestParam("alias") String alias){
        Map<String, List<Attendance>> resultMap=new HashMap<>();

        List<Attendance> attendanceList=mSumService.getSumOfAllTypesByAlias(alias);

        resultMap.put(KEY,attendanceList);

        return resultMap;
    }

    @GetMapping("/range")
    public Map<String, List<Attendance>> getAttendanceListByRange(
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ){
        Map<String, List<Attendance>> resultMap=new HashMap<>();

        List<Attendance> attendanceList=mSumService.getAllSumByDateRange(from,to);

        resultMap.put(KEY,attendanceList);

        return resultMap;
    }

    @GetMapping("/range_and_alias")
    public Map<String, List<Attendance>> getAttendanceListByRangeAndAlias(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("alias") String alias
    ){
        Map<String, List<Attendance>> resultMap=new HashMap<>();

        List<Attendance> attendanceList=mSumService.getSumByDateRangeAndAlias(from, to, alias);

        resultMap.put(KEY,attendanceList);

        return resultMap;
    }
}
