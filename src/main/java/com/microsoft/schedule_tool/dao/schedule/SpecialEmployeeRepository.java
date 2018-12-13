package com.microsoft.schedule_tool.dao.schedule;

import com.microsoft.schedule_tool.entity.schedule.SpecialEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Frank Hon on 11/26/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface SpecialEmployeeRepository extends JpaRepository<SpecialEmployee,Integer> {
}
