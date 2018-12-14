package com.microsoft.schedule_tool.schedule.service;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/14
 **/
public interface MutexEmployeeService {
    List<List<Long>> getAllMutexGroup();

    void addMutexEmployee(String ids);
}
