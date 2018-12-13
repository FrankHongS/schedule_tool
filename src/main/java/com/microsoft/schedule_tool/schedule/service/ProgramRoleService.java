package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
public interface ProgramRoleService {
    //add a ProgramRole
    long save(long programId, String name, String scheduleCycle,int workDays);

    //remove a ProgramRole
    void remove(long id);

    //change ProgramRole name;
    void update(long id, String newName,String cycle,int workDays);

    //save some ProgramRole one time
    long[] saveSome(String jsonData);

    //get all ProgramRole
    List<ProgramRole> findAllByProgram(long programId);
}
