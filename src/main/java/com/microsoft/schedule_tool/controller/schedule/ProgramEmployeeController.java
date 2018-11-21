package com.microsoft.schedule_tool.controller.schedule;

import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;
import com.microsoft.schedule_tool.service.schedule.ProgramEmployeeService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/program_employee")
public class ProgramEmployeeController {

    private static final String KEY="program_employee";

    @Autowired
    private ProgramEmployeeService mProgramEmployeeService;

    @GetMapping
    public Result getAllEmployeesByProgramId(@RequestParam("programId") Integer programId){
        Map<String,List<ProgramEmployee>> resultMap=new HashMap<>();

        List<ProgramEmployee> programEmployeeList=mProgramEmployeeService.getAllProgramEmployeesByProgramId(programId);
        resultMap.put(KEY,programEmployeeList);

        return ResultUtil.success(resultMap);
    }

    @PostMapping
    public Result saveProgramEmployee(@RequestParam("name") String name,@RequestParam("programId") Integer programId){
        Map<String,ProgramEmployee> resultMap=new HashMap<>();

        ProgramEmployee programEmployee=new ProgramEmployee();
        programEmployee.setName(name);
        programEmployee.setProgramId(programId);

        ProgramEmployee result=mProgramEmployeeService.saveProgramEmployee(programEmployee);

        resultMap.put(KEY,result);

        return ResultUtil.success(resultMap);
    }

    @PostMapping("/update")
    public Result updateProgramEmployee(@RequestParam("id") Integer id,@RequestParam("name") String name){
        Map<String,ProgramEmployee> resultMap=new HashMap<>();

        ProgramEmployee result=mProgramEmployeeService.updateProgramEmployee(id, name);
        resultMap.put(KEY,result);

        return ResultUtil.success(resultMap);
    }

    @PostMapping("/delete")
    public Result deleteProgramEmployee(@RequestParam("id") Integer id){
        mProgramEmployeeService.deleteProgramEmployee(id);

        return ResultUtil.success();
    }
}