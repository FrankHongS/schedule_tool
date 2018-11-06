package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.entity.LateType;
import com.microsoft.schedule_tool.service.LateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    
    @GetMapping(value = "")
    public Map<String, List<LateType>> getLatesByAlias(
            @RequestParam(name = "alias") String alias){
        Map<String,List<LateType>> resultMap=new HashMap<>();
        List<LateType> leaveList=mLateService.getAllLatesByAlias(alias);
        resultMap.put(KEY,leaveList);
        return resultMap;
    }

    @PostMapping(value = "")
    public Map<String, LateType> saveLate(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "alias") String alias,
            @RequestParam(name = "lateDate") String lateDate,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String,LateType> resultMap=new HashMap<>();
        LateType late=new LateType();
        late.setName(name);
        late.setAlias(alias);
        late.setLateDate(lateDate);
        late.setNormal(isNormal);
        late.setComment(comment);

        LateType result=mLateService.saveLate(late);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping(value = "/update")
    public Map<String, LateType> updateLate(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "lateDate",required = false) String lateDate,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String,LateType> resultMap=new HashMap<>();

        LateType result=mLateService.updateLate(id,lateDate,comment,isNormal);
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
