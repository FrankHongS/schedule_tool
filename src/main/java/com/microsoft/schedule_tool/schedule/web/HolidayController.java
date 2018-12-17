package com.microsoft.schedule_tool.schedule.web;

import com.microsoft.schedule_tool.schedule.domain.entity.Holiday;
import com.microsoft.schedule_tool.schedule.repository.HolidayRepository;
import com.microsoft.schedule_tool.schedule.service.HolidayService;
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
@RequestMapping("/holiday")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @PostMapping("/add")
    public Result add(@RequestParam("holidays") String holidays) {
        holidayService.addHolidays(holidays);
        return ResultUtil.success();
    }

    @GetMapping()
    public Result getAll(String from, String to) {
        List<Holiday> holidays = holidayService.getHolidays(from, to);
        Map<String, List<Holiday>> data = ResultUtil.getResultData("holidays", holidays);
        return ResultUtil.success(data);
    }

    @GetMapping("/delete")
    public Result delete(String date) {
        holidayService.deleteHoliday(date);
        return ResultUtil.success();
    }
}
