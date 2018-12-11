package com.microsoft.schedule_tool.controller.attendance;

import com.microsoft.schedule_tool.entity.Late;
import com.microsoft.schedule_tool.service.LateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */

@RestController
@RequestMapping("/late")
public class LateController {

    private static final String KEY="late";
    
    @Autowired
    private LateService mLateService;

    @GetMapping
    public Map<String, List<Late>> getLatesByEmployeeId(
            @RequestParam(name = "employeeId") Integer employeeId){
        Map<String,List<Late>> resultMap=new HashMap<>();
        List<Late> lateList=mLateService.getAllLatesByEmployeeId(employeeId);
        resultMap.put(KEY,lateList);
        return resultMap;
    }

    @GetMapping("/type")
    public Map<String, List<Late>> getLatesByEmployeeIdAndLateType(
            @RequestParam(name = "employeeId") Integer employeeId,
            @RequestParam(name = "lateType") Integer lateType){
        Map<String,List<Late>> resultMap=new HashMap<>();
        List<Late> lateList=mLateService.getAllLatesByEmployeeIdAndLateType(employeeId, lateType);
        resultMap.put(KEY,lateList);
        return resultMap;
    }


    @GetMapping("/range_and_type")
    public Map<String, List<Late>> getLeavesByEmployeeIdAndLeaveTypeAndRange(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam(name = "employeeId") Integer employeeId,
            @RequestParam(name = "leaveType") Integer lateType){
        Map<String,List<Late>> resultMap=new HashMap<>();
        List<Late> lateList=mLateService.getAllLatesByDateRangeAndEmployeeIdAndLeaveType(from, to, employeeId, lateType);
        resultMap.put(KEY,lateList);
        return resultMap;
    }

    @GetMapping("/range")
    public Map<String, List<Late>> getLeavesByEmployeeIdAndRange(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam(name = "employeeId") Integer employeeId){
        Map<String,List<Late>> resultMap=new HashMap<>();
        List<Late> lateList=mLateService.getAllLatesByDateRangeAndEmployeeId(from, to, employeeId);
        resultMap.put(KEY,lateList);
        return resultMap;
    }

    @GetMapping("/recent")
    public Map<String, List<Late>> getRecentLates(@RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size){
        Map<String,List<Late>> resultMap=new HashMap<>();
        List<Late> lateList=mLateService.getAllLatesOrderByCreatedTime(page, size);
        resultMap.put(KEY,lateList);
        return resultMap;
    }

    @PostMapping(value = "")
    public Map<String, Late> saveLate(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "alias") String alias,
            @RequestParam(name = "lateType") Integer lateType,
            @RequestParam(name = "lateDate") String lateDate,
            @RequestParam(name = "employeeId") Integer employeeId,
            @RequestParam(name = "isNormal") Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String, Late> resultMap=new HashMap<>();

        Late result=mLateService.saveLate(name, alias, lateType, lateDate, employeeId, isNormal, comment);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping(value = "/update")
    public Map<String, Late> updateLate(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "lateType") Integer lateType,
            @RequestParam(name = "lateDate") String lateDate,
            @RequestParam(name = "isNormal") Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String, Late> resultMap=new HashMap<>();

        Late result=mLateService.updateLate(id,lateType,lateDate,comment,isNormal);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping("/delete")
    public Map<String, String> deleteLate(@RequestParam("id") Integer id){
        Map<String,String> resultMap=new HashMap<>();

        if(mLateService.deleteLate(id)){
            resultMap.put(KEY,"delete is success");
        }else{
            resultMap.put(KEY,"delete is failure");
        }

        return resultMap;
    }
}
