package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.LastScheduleTime;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kb_jay
 * @time 2019/1/18
 **/
public interface LastScheduleTimeResposity extends JpaRepository<LastScheduleTime,Long> {
    //1.save   2.clear
    void deleteById(Long id);
}
