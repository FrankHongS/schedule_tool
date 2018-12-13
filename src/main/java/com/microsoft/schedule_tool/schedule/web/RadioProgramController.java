package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import com.microsoft.schedule_tool.schedule.service.RadioProgramService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import java.util.List;
import java.util.Map;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@RestController
@RequestMapping("/sprogram")
public class RadioProgramController {

    @Autowired
    private RadioProgramService radioProgramService;

    @PostMapping("/add")
    public Result save(
            @RequestParam("stationId")
                    long stationId,
            @RequestParam("name")
                    String name) {
        System.out.println("call save!!!!!!");
        long save = radioProgramService.save(stationId, name);
        Long s = save;
        Map<String, Long> data = ResultUtil.getResultData("id", s);
        return ResultUtil.success(data);
    }

    @GetMapping("/programs")
    public Result getPrograms(
            @RequestParam("stationId") long stationId) {
        List<RadioProgram> programs = radioProgramService.findAllByStation(stationId);
        Map<String, List<RadioProgram>> programs1 = ResultUtil.getResultData("programs", programs);
        return ResultUtil.success(programs1);
    }

    @GetMapping("/delete")
    public Result deleteProgram(@RequestParam("programId") long programId) {
        radioProgramService.remove(programId);
        return ResultUtil.success();
    }

    @PostMapping("/update")
    public Result updateProgram(@RequestParam("programId") long programId,
                                @RequestParam("name") String newName) {
        radioProgramService.changeName(programId, newName);
        return ResultUtil.success();
    }

    @PostMapping("/addSome")
    public Result saveSome(@RequestParam("programs") String programs) {
        long[] ids = radioProgramService.saveSome(programs);
        Map<String, long[]> ids1 = ResultUtil.getResultData("ids", ids);
        return ResultUtil.success(ids1);
    }
}
