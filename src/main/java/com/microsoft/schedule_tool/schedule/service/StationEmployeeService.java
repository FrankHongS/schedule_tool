package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/

public interface StationEmployeeService {
    //add a program
    long save(String name, String alias);

    //remove a program
    void remove(long id);

    //change program name;
    void update(long id, String name, String alias);

    //save some programs one time
    long[] saveSome(String jsonData);

    //get all programs
    List<StationEmployee> findAll();
}
