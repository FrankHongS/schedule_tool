package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioReplaceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kb_jay
 * @time 2018/12/18
 **/
public interface RadioReplaceScheduleReposity extends JpaRepository<RadioReplaceSchedule,Long> {
    void deleteById(Long id);
}
