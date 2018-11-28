package com.microsoft.schedule_tool.controller.schedule;

import com.microsoft.schedule_tool.entity.schedule.Substitute;
import com.microsoft.schedule_tool.service.schedule.SubstituteService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/28/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/substitute")
public class SubstituteController {

    private static final String KEY="substitute";

    @Autowired
    private SubstituteService mSubstituteService;

    @GetMapping
    public Result getAllSubstitute(@RequestParam("isHoliday") Boolean isHoliday){
        Map<String, List<Substitute>> resultMap=new HashMap<>();

        List<Substitute> substituteList=mSubstituteService.getAllHolidayOrNotHolidaySubstitutes(isHoliday);

        resultMap.put(KEY,substituteList);

        return ResultUtil.success(resultMap);
    }

    @GetMapping("/range")
    public Result getAllSubstituteInDateRange(@RequestParam("isHoliday") Boolean isHoliday,
                                              @RequestParam("from") String from,
                                              @RequestParam("to") String to){
        Map<String, List<Substitute>> resultMap=new HashMap<>();

        List<Substitute> substituteList=mSubstituteService.getAllSubstitutesInDataRange(isHoliday,from,to);

        resultMap.put(KEY,substituteList);

        return ResultUtil.success(resultMap);
    }

    @PostMapping
    public Result saveSubstitute(@RequestParam("subDate") String subDate,
                                 @RequestParam("subProgram") String subProgram,
                                 @RequestParam("subName") String subName,
                                 @RequestParam("isHoliday") Boolean isHoliday){
        Map<String,Substitute> resultMap=new HashMap<>();

        Substitute substitute=mSubstituteService.saveSubstitute(subDate, subProgram, subName, isHoliday);

        resultMap.put(KEY,substitute);

        return ResultUtil.success(resultMap);
    }


}
