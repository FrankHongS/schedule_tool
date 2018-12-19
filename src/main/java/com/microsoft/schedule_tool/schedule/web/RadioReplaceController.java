package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.domain.vo.response.RespReplaceSchedule;
import com.microsoft.schedule_tool.schedule.service.RadioReplaceScheduleService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author kb_jay
 * @time 2018/12/18
 **/
@RestController
@RequestMapping("/replace")
public class RadioReplaceController {
    @Autowired
    private RadioReplaceScheduleService radioReplaceScheduleService;

    @GetMapping()
    public Result findReplace(String from, String to) {
        List<RespReplaceSchedule> allReplace = radioReplaceScheduleService.getAllReplace(from, to);
        Map<String, List<RespReplaceSchedule>> data = ResultUtil.getResultData("data", allReplace);
        return ResultUtil.success(data);
    }

    @PostMapping("/add")
    public Result addReplace(long roleId, String date, long employeeId) {
        long l = radioReplaceScheduleService.addReplace(roleId, date, employeeId);
        Map<String, Long> data = ResultUtil.getResultData("id", l);
        return ResultUtil.success(data);
    }

    @PostMapping("/delete")
    public Result deleteReplace(long id) {
        radioReplaceScheduleService.deleteReplace(id);
        return ResultUtil.success();
    }

    @GetMapping("/findAll")
    public Result findAll(){
        List<RespReplaceSchedule> allReplace = radioReplaceScheduleService.getAllReplace();
        Map<String, List<RespReplaceSchedule>> data = ResultUtil.getResultData("data", allReplace);
        return ResultUtil.success(data);

    }
}
