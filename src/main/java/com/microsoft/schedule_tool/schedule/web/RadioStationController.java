package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.service.RadioStationService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kb_jay
 * @time 2018/12/11
 **/
@RestController
public class RadioStationController {
    @Autowired
    private RadioStationService radioStationService;

    @GetMapping("/init")
    public Result initStation() {
        radioStationService.init();
        return ResultUtil.success();
    }
}
