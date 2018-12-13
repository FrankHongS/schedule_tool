package com.microsoft.schedule_tool.service.schedule;

import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;

import java.util.List;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ProgramEmployeeService {

    List<ProgramEmployee> getAllProgramEmployeesByProgramId(Integer programId);

    /**
     *
     * @param programId 节目id
     * @param employeeType 员工类型
     * @return
     */
    List<ProgramEmployee> getAllProgramEmployeesByProgramIdAndEmployeeType(Integer programId, Integer employeeType);

    ProgramEmployee saveProgramEmployee(ProgramEmployee programEmployee,Integer programId);

    ProgramEmployee updateProgramEmployee(Integer id,String name, Integer employeeType);

    boolean deleteProgramEmployee(Integer id,Integer programId);
}
