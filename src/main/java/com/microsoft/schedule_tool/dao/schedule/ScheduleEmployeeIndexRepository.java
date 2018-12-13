package com.microsoft.schedule_tool.dao.schedule;

import com.microsoft.schedule_tool.entity.schedule.ScheduleEmployeeIndex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Frank Hon on 11/29/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ScheduleEmployeeIndexRepository extends JpaRepository<ScheduleEmployeeIndex,Integer> {

    Optional<ScheduleEmployeeIndex> findByEmployeeType(Integer employeeType);
}
