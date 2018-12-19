package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.domain.vo.response.MutextEmployeesResp;
import com.microsoft.schedule_tool.schedule.service.MutexEmployeeService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kb_jay
 * @time 2018/12/14
 **/
@RestController
@RequestMapping("/mutex_employee")
public class MutexEmployeeController {
    @Autowired
    private MutexEmployeeService mutexEmployeeService;

    @GetMapping
    public Result getAllMutex() {
        List<MutextEmployeesResp> allMutexGroup = mutexEmployeeService.getAllMutexGroup();
        Map<String, List<MutextEmployeesResp>> data = ResultUtil.getResultData("data", allMutexGroup);
        return ResultUtil.success(data);
    }

    @PostMapping("/add")
    public Result addMutexs(@RequestParam("ids") String ids) {
        mutexEmployeeService.addMutexEmployee(ids);
        return ResultUtil.success();
    }

    @PostMapping("/update")
    public Result updateMutexs(@RequestParam("id") long id,
                               @RequestParam("ids") String ids) {
        mutexEmployeeService.updateMutexEmployee(id, ids);
        return ResultUtil.success();
    }

    @PostMapping("/delete")
    public Result delete(long id) {
        mutexEmployeeService.delete(id);
        return ResultUtil.success();
    }
}
