package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/13
 **/
public interface RadioScheduleRepository extends JpaRepository<RadioSchedule, Long> {
    //存放数据
    //导出数据
    List<RadioSchedule> findAllByDateLessThanEqualAndDateGreaterThanEqual(Date to,Date from);
}
