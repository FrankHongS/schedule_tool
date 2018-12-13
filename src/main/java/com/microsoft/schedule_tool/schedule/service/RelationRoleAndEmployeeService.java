package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.repository.RelationRoleAndEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
public interface RelationRoleAndEmployeeService {
    //getAllWorkersByRoleId
    //addWorkers2Role
    //removeWorkersByRole
    //changeRatio

    List<StationEmployee> getAllWorkersByRoleId(long id);

    void addWorkers2Role(long employeeId, long roleId, int ratio);

    void removeWorkersByRole(long employeeId, long roleId);

    void changeRatio(long employeeId, long roleId, int ratio);

    int getRatio(long employeeId, long roleId);

}
