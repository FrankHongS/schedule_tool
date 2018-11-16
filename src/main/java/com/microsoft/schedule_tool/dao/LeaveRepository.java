package com.microsoft.schedule_tool.dao;

import com.microsoft.schedule_tool.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */
public interface LeaveRepository extends JpaRepository<Leave,Integer> {

    List<Leave> findByAlias(String alias);

    List<Leave> findByEmployeeId(Integer employeeId);

    List<Leave> findByEmployeeIdAndLeaveType(Integer employeeId,Integer leaveType);

    List<Leave> findByCreatedTimeAfterAndCreatedTimeBeforeAndAlias(Date from,Date to,String alias);

    List<Leave> findByCreatedTimeAfterAndCreatedTimeBefore(Date from, Date to);

    List<Leave> findByFromAfterAndFromBeforeAndAlias(Date from,Date to,String alias);

    List<Leave> findByFromAfterAndFromBefore(Date from, Date to);

    List<Leave> findByFromIsAfterAndFromBeforeAndEmployeeIdAndLeaveType(Date from,Date to,Integer employeeId,Integer leaveType);

    List<Leave> findByFromIsAfterAndFromBeforeAndEmployeeId(Date from,Date to,Integer employeeId);

    void deleteByEmployeeId(Integer employeeId);
}
