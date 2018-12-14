package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.repository.HolidayRepository;
import com.microsoft.schedule_tool.schedule.service.HolidayService;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kb_jay
 * @time 2018/12/14
 **/
@RestController
@RequestMapping("/holiday")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @PostMapping("/add")
    public Result add(@RequestParam("holidays") String holidays) {
        holidayService.addHolidays(holidays);
        return ResultUtil.success();
    }
}
