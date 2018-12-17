package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.domain.vo.response.EquelRoleResp;
import com.microsoft.schedule_tool.schedule.service.EqualRolesService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author kb_jay
 * @time 2018/12/17
 **/
@RestController
@RequestMapping("/equal_role")
public class EqualRoleController {
    @Autowired
    private EqualRolesService equalRolesService;

    @GetMapping
    public Result getEqualRoles() {
        List<EquelRoleResp> allEqualRolesGroup = equalRolesService.getAllEqualRolesGroup();
        Map<String, List<EquelRoleResp>> data = ResultUtil.getResultData("data", allEqualRolesGroup);
        return ResultUtil.success(data);
    }

    @PostMapping("/add")
    public Result addEqualRoles(String ids) {
        equalRolesService.addEqualRoles(ids);
        return ResultUtil.success();
    }

    @PostMapping("/update")
    public Result updateEqualRoles(long id, String ids) {
        equalRolesService.updateEqualRoles(id, ids);
        return ResultUtil.success();
    }
}
