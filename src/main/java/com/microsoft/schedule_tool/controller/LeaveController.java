package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.entity.Leave;
import com.microsoft.schedule_tool.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */
@RestController
@RequestMapping("/leave")
public class LeaveController {

    private static final String KEY="leave";

    @Autowired
    private LeaveService leaveService;

    @GetMapping
    public Map<String, List<Leave>> getLeavesByEmployeeId(
            @RequestParam(name = "employeeId") Integer employeeId){
        Map<String,List<Leave>> resultMap=new HashMap<>();
        List<Leave> leaveList=leaveService.getAllLeavesByEmployeeId(employeeId);
        resultMap.put(KEY,leaveList);
        return resultMap;
    }

    @GetMapping("/type")
    public Map<String, List<Leave>> getLeavesByEmployeeIdAndLeaveType(
            @RequestParam(name = "employeeId") Integer employeeId,
            @RequestParam(name = "leaveType") Integer leaveType){
        Map<String,List<Leave>> resultMap=new HashMap<>();
        List<Leave> leaveList=leaveService.getAllLeavesByEmployeeIdAndLeaveType(employeeId, leaveType);
        resultMap.put(KEY,leaveList);
        return resultMap;
    }

    @PostMapping
    public Map<String, Leave> saveLeave(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "alias") String alias,
            @RequestParam(name = "leaveType") Integer leaveType,
            @RequestParam(name = "leaveDateRange") String leaveDateRange,
            @RequestParam(name = "dayCount") Integer dayCount,
            @RequestParam(name = "employeeId") Integer employeeId,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String, Leave> resultMap=new HashMap<>();
        Leave leave=new Leave();
        leave.setName(name);
        leave.setAlias(alias);
        leave.setLeaveType(leaveType);
        leave.setLeaveDateRange(leaveDateRange);
        leave.setDayCount(dayCount);
        leave.setEmployeeId(employeeId);
        leave.setNormal(isNormal);
        leave.setComment(comment);

        Leave result=leaveService.saveLeave(leave);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping(value = "/update")
    public Map<String, Leave> updateLeave(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "leaveType") Integer leaveType,
            @RequestParam(name = "leaveDateRange") String leaveDateRange,
            @RequestParam(name = "dayCount") Integer dayCount,
            @RequestParam(name = "isNormal") Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String, Leave> resultMap=new HashMap<>();

        Leave result=leaveService.updateLeave(id,leaveType,leaveDateRange,dayCount,comment,isNormal);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping("/delete")
    public Map<String, String> deleteLeave(@RequestParam("id") Integer id){
        Map<String,String> resultMap=new HashMap<>();

        if(leaveService.deleteLeave(id)){
            resultMap.put(KEY,"delete is success");
        }else{
            resultMap.put(KEY,"delete is failure");
        }

        return resultMap;
    }
}
