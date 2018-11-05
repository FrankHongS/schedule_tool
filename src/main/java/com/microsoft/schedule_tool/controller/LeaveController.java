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

    @Autowired
    private LeaveService leaveService;

    @GetMapping(value = "")
    public Map<String, List<LeaveType>> getLeavesByAlias(
            @RequestParam(name = "alias") String alias){
        Map<String,List<LeaveType>> leaveMap=new HashMap<>();
        List<LeaveType> leaveList=leaveService.getAllLeavesByAlias(alias);
        leaveMap.put("leave",leaveList);
        return leaveMap;
    }

    @PostMapping(value = "")
    public Map<String, LeaveType> saveLeave(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "alias") String alias,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String,LeaveType> leaveMap=new HashMap<>();
        LeaveType leave=new LeaveType();
        leave.setName(name);
        leave.setAlias(alias);
        leave.setNormal(isNormal);
        leave.setComment(comment);
        if(leaveService.saveLeave(leave)){
            leaveMap.put("leave",leave);
        }else{
            leaveMap.put("leave",null);
        }

        return leaveMap;
    }

    @PostMapping(value = "/update")
    public Map<String, String> updateLeave(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String,String> leaveMap=new HashMap<>();
        if(leaveService.updateLeave(id,comment,isNormal)){
            leaveMap.put("leave","success");
        }else{
            leaveMap.put("leave","fail");
        }

        return leaveMap;
    }
}
