package com.microsoft.schedule_tool.service.schedule;

import com.microsoft.schedule_tool.vo.schedule.ProgramSchedule;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainer;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainerTest;

import java.util.List;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ScheduleExcelService {

    ProgramScheduleContainer getProgramSchedule(String from, String to);

    ProgramScheduleContainerTest getProgramScheduleTest(String from, String to);
}
