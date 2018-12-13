package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RelationRoleAndEmployee;
import com.microsoft.schedule_tool.schedule.domain.entity.RoleEmployeeMultiKeysClass;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
public interface RelationRoleAndEmployeeRepository extends JpaRepository<RelationRoleAndEmployee, RoleEmployeeMultiKeysClass> {
    //getAllWorkersByRoleId
    //addWorkers2Role
    //removeWorkersByRole
    //changeRatio
    List<RelationRoleAndEmployee> getAllByRoleId(long roleId);
}
