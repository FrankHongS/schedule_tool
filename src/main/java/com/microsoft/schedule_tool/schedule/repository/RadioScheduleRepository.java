package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/13
 **/
public interface RadioScheduleRepository extends JpaRepository<RadioSchedule, Long> {
    //存放数据
    //导出数据
    List<RadioSchedule> findAllByDateLessThanEqualAndDateGreaterThanEqual(Date to, Date from);

    @Modifying
    @Transactional
    void deleteByDateLessThanEqualAndDateGreaterThanEqual(Date big, Date small);
}
