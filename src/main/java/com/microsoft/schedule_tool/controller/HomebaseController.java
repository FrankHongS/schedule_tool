package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.entity.HomebaseType;
import com.microsoft.schedule_tool.service.HomebaseService;
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
@RequestMapping("/homebase")
public class HomebaseController {

    private static final String KEY="homebase";

    @Autowired
    private HomebaseService mHomebaseService;

    @GetMapping(value = "")
    public Map<String, List<HomebaseType>> getLatesByAlias(
            @RequestParam(name = "alias") String alias){
        Map<String,List<HomebaseType>> resultMap=new HashMap<>();
        List<HomebaseType> leaveList=mHomebaseService.getAllHomebasesByAlias(alias);
        resultMap.put(KEY,leaveList);
        return resultMap;
    }

    @PostMapping
    public Map<String, HomebaseType> saveLate(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "alias") String alias,
            @RequestParam(name = "homebaseDate") String homebaseDate,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String,HomebaseType> resultMap=new HashMap<>();
        HomebaseType homebase=new HomebaseType();
        homebase.setName(name);
        homebase.setAlias(alias);
        homebase.setHomebaseDate(homebaseDate);
        homebase.setNormal(isNormal);
        homebase.setComment(comment);

        HomebaseType result=mHomebaseService.saveHomebase(homebase);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping(value = "/update")
    public Map<String, HomebaseType> updateLate(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "homebaseDate",required = false) String homebaseDate,
            @RequestParam(name = "isNormal",required = false) Boolean isNormal,
            @RequestParam(name = "comment",required = false) String comment){
        Map<String,HomebaseType> resultMap=new HashMap<>();

        HomebaseType result=mHomebaseService.updateHomebase(id,homebaseDate,comment,isNormal);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping("/delete")
    public Map<String, String> deleteLate(@RequestParam("id") Integer id){
        Map<String,String> resultMap=new HashMap<>();

        if(mHomebaseService.deleteHomebase(id)){
            resultMap.put(KEY,"delete is success");
        }else{
            resultMap.put(KEY,"delete is failure");
        }

        return resultMap;
    }
}
