package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.ScheduleStates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/24
 **/
public interface ScheduleStatesResposity extends JpaRepository<ScheduleStates,Long> {

    //1：获取某年某周某个角色的状态
    Optional<ScheduleStates> findByYearAndWeekAndRole(String year, int week, ProgramRole role);

    //2: 更新某年某周某个角色的状态


}
