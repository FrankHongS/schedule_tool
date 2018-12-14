package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.vo.response.MutextEmployeesResp;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/14
 **/
public interface MutexEmployeeService {
    List<MutextEmployeesResp> getAllMutexGroup();

    void addMutexEmployee(String ids);

    void updateMutexEmployee(long id, String ids);
}
