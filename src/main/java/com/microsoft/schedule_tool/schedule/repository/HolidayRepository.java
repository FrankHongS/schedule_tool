package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
public interface HolidayRepository extends JpaRepository<Holiday,Long> {

}
