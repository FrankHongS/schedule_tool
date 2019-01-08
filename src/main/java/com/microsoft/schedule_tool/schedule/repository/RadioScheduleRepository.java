package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/13
 **/
public interface RadioScheduleRepository extends JpaRepository<RadioSchedule, Long> {
    //导出排班表中的数据
    List<RadioSchedule> findAllByDateLessThanEqualAndDateGreaterThanEqualAndIsHoliday(Date to, Date from, boolean isHoliday);
    //添加更改节假日数据

    Optional<RadioSchedule> findByDateAndRole(Date date, ProgramRole role);

    List<RadioSchedule> findAllByIsHoliday(boolean isHoliday);

    @Modifying
    @Transactional
    void deleteByDateLessThanEqualAndDateGreaterThanEqual(Date big, Date small);

    @Modifying
    @Transactional
    void deleteByDateGreaterThanEqual(Date small);

    List<RadioSchedule> findAllByRoleAndDateLessThanEqualAndDateGreaterThanEqualAndIsHoliday(ProgramRole role, Date big, Date small, boolean isHoliday);
}
