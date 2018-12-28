package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.ScheduleStates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/24
 **/
public interface ScheduleStatesResposity extends JpaRepository<ScheduleStates, Long> {
    //get first date by current date and roleId
    Optional<ScheduleStates> getByCurrentDateAndRole(Date date, ProgramRole role);

}
