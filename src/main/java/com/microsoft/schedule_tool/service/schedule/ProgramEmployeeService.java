package com.microsoft.schedule_tool.service.schedule;

import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;

import java.util.List;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ProgramEmployeeService {

    List<ProgramEmployee> getAllProgramEmployeesByProgramId(Integer programId);

    ProgramEmployee saveProgramEmployee(ProgramEmployee programEmployee);

    ProgramEmployee updateProgramEmployee(Integer id,String name);

    boolean deleteProgramEmployee(Integer id);
}
