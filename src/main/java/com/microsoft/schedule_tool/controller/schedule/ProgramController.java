package com.microsoft.schedule_tool.controller.schedule;

import com.microsoft.schedule_tool.entity.schedule.Program;
import com.microsoft.schedule_tool.service.schedule.ProgramService;
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
@RequestMapping("/program")
public class ProgramController {

    private static final String KEY="program";

    @Autowired
    private ProgramService mProgramService;

    @GetMapping
    public Result getAllPrograms(){
        Map<String,List<Program>> resultMap=new HashMap<>();

        List<Program> programList=mProgramService.getAllPrograms();
        resultMap.put(KEY,programList);

        return ResultUtil.success(resultMap);
    }

    @PostMapping
    public Result saveProgram(@RequestParam("name") String name){
        Program program=new Program();
        program.setName(name);

        Program result=mProgramService.saveProgram(program);
        Map<String,Program> resultMap=new HashMap<>();
        resultMap.put(KEY,result);

        return ResultUtil.success(resultMap);
    }

    @PostMapping("/update")
    public Result updateProgram(@RequestParam("id") Integer id,@RequestParam("name") String name){
        Program result=mProgramService.updateProgram(id, name);
        Map<String,Program> resultMap=new HashMap<>();
        resultMap.put(KEY,result);

        return ResultUtil.success(resultMap);
    }

    @PostMapping("/delete")
    public Result deleteProgram(@RequestParam("id") Integer id){
        mProgramService.deleteProgram(id);

        return ResultUtil.success();
    }
}
