package com.microsoft.schedule_tool.dao.schedule;

import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ProgramEmployeeRepository extends JpaRepository<ProgramEmployee,Integer> {

    List<ProgramEmployee> findByProgramId(Integer programId);

    Optional<ProgramEmployee> findByNameAndProgramId(String name,Integer programId);
}
