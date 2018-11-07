package com.microsoft.schedule_tool.controller;

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
        List<Late> leaveList=mLateService.getAllLatesByEmployeeId(employeeId);
        resultMap.put(KEY,leaveList);
        return resultMap;
    }

    @GetMapping("/type")
    public Map<String, List<Late>> getLatesByEmployeeIdAndLateType(
            @RequestParam(name = "employeeId") Integer employeeId,
            @RequestParam(name = "lateType") Integer lateType){
        Map<String,List<Late>> resultMap=new HashMap<>();
        List<Late> leaveList=mLateService.getAllLatesByEmployeeIdAndLateType(employeeId, lateType);
        resultMap.put(KEY,leaveList);
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
        Late late=new Late();
        late.setName(name);
        late.setAlias(alias);
        late.setLateType(lateType);
        late.setLateDate(lateDate);
        late.setEmployeeId(employeeId);
        late.setNormal(isNormal);
        late.setComment(comment);

        Late result=mLateService.saveLate(late);
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
