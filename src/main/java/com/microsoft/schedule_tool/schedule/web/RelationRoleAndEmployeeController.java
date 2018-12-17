package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespEmployeeByRoleId;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kb_jay
 * @time 2018/12/11
 **/
@RestController
@RequestMapping("/role_employee")
public class RelationRoleAndEmployeeController {

    @Autowired
    private RelationRoleAndEmployeeService relationRoleAndEmployeeService;

    //     List<StationEmployee> getAllWorkersByRoleId(long id);
    @GetMapping()
    public Result getAllWorkersByRoleId(@RequestParam("id") long id) {
        List<RespEmployeeByRoleId> employees = relationRoleAndEmployeeService.getAllWorkersByRoleId(id);
        Map<String, List<RespEmployeeByRoleId>> data = ResultUtil.getResultData("employees", employees);
        return ResultUtil.success(data);
    }

    //    void addWorkers2Role(long employeeId, long roleId, double ratio);
    @PostMapping("/add")
    public Result addWorkers2Role(@RequestParam("employeeId") long employeeId,
                                  @RequestParam("roleId") long roleId,
                                  @RequestParam("ratio") int ratio) {
        relationRoleAndEmployeeService.addWorkers2Role(employeeId, roleId, ratio);
        return ResultUtil.success();
    }

    //    void removeWorkersByRole(long employeeId, long roleId);
    @GetMapping("/delete")
    public Result removeWorkersByRole(@RequestParam("employeeId") long employeeId,
                                      @RequestParam("roleId") long roleId) {
        relationRoleAndEmployeeService.removeWorkersByRole(employeeId, roleId);
        return ResultUtil.success();
    }

    //    void changeRatio(long employeeId, long roleId, double ratio);
    @PostMapping("/update")
    public Result changeRatio(@RequestParam("employeeId") long employeeId,
                              @RequestParam("roleId") long roleId,
                              @RequestParam("ratio") int ratio) {
        relationRoleAndEmployeeService.changeRatio(employeeId, roleId, ratio);
        return ResultUtil.success();
    }
}
