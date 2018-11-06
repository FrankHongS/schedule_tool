package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.entity.LeaveType;
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

    @GetMapping(value = "")
    public Map<String, List<LeaveType>> getLeavesByAlias(
            @RequestParam(name = "alias") String alias){
        Map<String,List<LeaveType>> resultMap=new HashMap<>();
        List<LeaveType> leaveList=leaveService.getAllLeavesByAlias(alias);
        resultMap.put(KEY,leaveList);
        return resultMap;
    }

    @PostMapping(value = "")
    public Map<String, LeaveType> saveLeave(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "alias") String alias,
            @RequestParam(name = "leaveDateRange") String leaveDateRange,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String,LeaveType> resultMap=new HashMap<>();
        LeaveType leave=new LeaveType();
        leave.setName(name);
        leave.setAlias(alias);
        leave.setLeaveDateRange(leaveDateRange);
        leave.setNormal(isNormal);
        leave.setComment(comment);

        LeaveType result=leaveService.saveLeave(leave);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping(value = "/update")
    public Map<String, LeaveType> updateLeave(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "leaveDateRange",required = false) String leaveDateRange,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String,LeaveType> resultMap=new HashMap<>();

        LeaveType result=leaveService.updateLeave(id,leaveDateRange,comment,isNormal);
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
