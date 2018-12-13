package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.service.ProgramRoleService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
@RestController
@RequestMapping("/role")
public class ProgramRoleController {

    @Autowired
    private ProgramRoleService programRoleService;

    @PostMapping("/add")
    public Result saveRole(@RequestParam("programId") long programId,
                           @RequestParam("roleName") String roleName,
                           @RequestParam("cycle") String cycle,
                           @RequestParam("workDays") int workDays) {
        long save = programRoleService.save(programId, roleName, cycle, workDays);
        Map<String, Long> data = ResultUtil.getResultData("id", save);
        return ResultUtil.success(data);
    }

    @GetMapping("/delete")
    public Result deleteRole(@RequestParam("id") long id) {
        programRoleService.remove(id);
        return ResultUtil.success();
    }

    @PostMapping("/update")
    public Result update(@RequestParam("id") long id,
                         @RequestParam("roleName") String roleName,
                         @RequestParam("cycle") String cycle,
                         @RequestParam("workDays") int workDays) {
        programRoleService.update(id, roleName, cycle, workDays);
        return ResultUtil.success();
    }


    @PostMapping("/addSome")
    public Result addSome(@RequestParam("roles") String roles) {
        long[] longs = programRoleService.saveSome(roles);
        Map<String, long[]> ids = ResultUtil.getResultData("ids", longs);
        return ResultUtil.success(ids);
    }

    @GetMapping()
    public Result findAll(@RequestParam("programId") long programId) {
        List<ProgramRole> roles = programRoleService.findAllByProgram(programId);
        Map<String, List<ProgramRole>> data = ResultUtil.getResultData("roles", roles);
        return ResultUtil.success(data);
    }
}
