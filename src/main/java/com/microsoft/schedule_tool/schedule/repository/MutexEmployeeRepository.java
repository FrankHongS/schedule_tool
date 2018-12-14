package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.MutexEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kb_jay
 * @time 2018/12/14
 **/
public interface MutexEmployeeRepository extends JpaRepository<MutexEmployee, Long> {
    //

}
