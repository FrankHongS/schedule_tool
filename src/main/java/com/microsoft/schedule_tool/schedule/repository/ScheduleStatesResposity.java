package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.entity.schedule.Program;
import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.ScheduleStates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/24
 **/
public interface ScheduleStatesResposity extends JpaRepository<ScheduleStates, Long> {

    Optional<ScheduleStates> getByCurDateAndRole(Date date, ProgramRole role);

    Optional<ScheduleStates> getByRoleAndCurDate(ProgramRole role, Date curData);

    @Modifying
    @Transactional
    void deleteByCurDateGreaterThanEqual(Date date);

    @Modifying
    @Transactional
    void deleteByCurDateLessThanEqualAndCurDateGreaterThanEqual(Date big, Date small);
}
