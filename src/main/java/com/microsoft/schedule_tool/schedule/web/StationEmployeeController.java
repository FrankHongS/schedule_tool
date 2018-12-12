package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.service.StationEmployeeService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
@RestController
@RequestMapping("/station_employee")
public class StationEmployeeController {
    @Autowired
    private StationEmployeeService stationEmployeeService;

    @GetMapping()
    public Result findAll() {
        List<StationEmployee> all = stationEmployeeService.findAll();
        Map<String, List<StationEmployee>> data = ResultUtil.getResultData("employees", all);
        return ResultUtil.success(data);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam("id") long id) {
        stationEmployeeService.remove(id);
        return ResultUtil.success();
    }

    @PostMapping("/update")
    public Result update(@RequestParam("id") long id,
                         @RequestParam("name") String name,
                         @RequestParam("alias") String alias) {
        stationEmployeeService.update(id, name, alias);
        return ResultUtil.success();
    }

    @PostMapping("/add")
    public Result add(@RequestParam("name") String name,
                      @RequestParam("alias") String alias) {
        long save = stationEmployeeService.save(name, alias);
        return ResultUtil.success(ResultUtil.getResultData("id", save));
    }

    @PostMapping("/addSome")
    public Result addSome(@RequestParam("employees") String employees) {
        long[] longs = stationEmployeeService.saveSome(employees);
        return ResultUtil.success(ResultUtil.getResultData("ids", longs));
    }

}
