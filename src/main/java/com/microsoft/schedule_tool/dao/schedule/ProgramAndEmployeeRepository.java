package com.microsoft.schedule_tool.dao.schedule;

import com.microsoft.schedule_tool.entity.schedule.ProgramAndEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Frank Hon on 11/22/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ProgramAndEmployeeRepository extends JpaRepository<ProgramAndEmployee,Integer> {

    List<ProgramAndEmployee> findByProgramId(Integer programId);

    List<ProgramAndEmployee> findByEmployeeId(Integer employeeId);

    Optional<ProgramAndEmployee> findByProgramIdAndEmployeeId(Integer programId,Integer employeeId);

    void deleteByEmployeeId(Integer employeeId);

    void deleteByProgramId(Integer programId);

    void deleteByProgramIdAndEmployeeId(Integer programId,Integer employeeId);
}
