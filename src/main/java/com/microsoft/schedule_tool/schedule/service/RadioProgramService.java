package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioStation;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/

//1：add


//2：remove

//3：update

//4：addSome

//5：findAllByStation
public interface RadioProgramService {
    //add a program
    long save(long stationId, String name);
    //remove a program
    void remove(long id);
    //change program name;
    void changeName(long id, String newName);
    //save some programs one time
    long[] saveSome(String jsonData);
    //get all programs
    List<RadioProgram> findAllByStation(long stationId);
}
