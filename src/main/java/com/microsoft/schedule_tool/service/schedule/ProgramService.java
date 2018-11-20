package com.microsoft.schedule_tool.service.schedule;

import com.microsoft.schedule_tool.entity.schedule.Program;

import java.util.List;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ProgramService {

    List<Program> getAllPrograms();

    Program saveProgram(Program program);

    Program updateProgram(Integer id,String name);

    boolean deleteProgram(Integer id);
}
