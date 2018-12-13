package com.microsoft.schedule_tool.controller.schedule;

import com.microsoft.schedule_tool.entity.schedule.SpecialEmployee;
import com.microsoft.schedule_tool.service.schedule.SpecialEmployeeService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/26/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/special")
public class SpecialEmployeeController {

    private static final String KEY="special";

    @Autowired
    private SpecialEmployeeService mSpecialEmployeeService;

    @GetMapping
    public Result getAllSpecialEmployees(){
        Map<String, List<SpecialEmployee>> resultMap=new HashMap<>();

        List<SpecialEmployee> specialEmployeeList=mSpecialEmployeeService.getAllSpecialEmployees();

        resultMap.put(KEY,specialEmployeeList);

        return ResultUtil.success(resultMap);
    }

    @PostMapping
    public Result saveSpecialEmployee(@RequestParam("name") String name){
        Map<String,SpecialEmployee> resultMap=new HashMap<>();

        SpecialEmployee specialEmployee=new SpecialEmployee();
        specialEmployee.setName(name);

        SpecialEmployee result=mSpecialEmployeeService.saveSpecialEmployee(specialEmployee);

        resultMap.put(KEY,result);

        return ResultUtil.success(resultMap);
    }

    @PostMapping("/delete")
    public Result deleteSpecialEmployee(@RequestParam("id") Integer id){
        mSpecialEmployeeService.deleteSpecialEmployee(id);

        return ResultUtil.success();
    }
}
