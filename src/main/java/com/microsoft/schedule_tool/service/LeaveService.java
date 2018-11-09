package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.Leave;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */
public interface LeaveService {

    List<Leave> getAllLeavesByEmployeeId(Integer employeeId);

    List<Leave> getAllLeavesByEmployeeIdAndLeaveType(Integer employeeId, Integer leaveType);

    List<Leave> getAllLeavesByDateRangeAndAlias(Date from, Date to, String alias);

    List<Leave> getAllLeavesByDateRange(Date from, Date to);

    List<Leave> getAllLeavesByDateRangeAndEmployeeIdAndLeaveType(String from, String to, Integer employeeId, Integer leaveType);

    List<Leave> getAllLeavesByDateRangeAndEmployeeId(String from, String to, Integer employeeId);

    Leave saveLeave(Leave leave);

    Leave saveLeave(String name, String alias, Integer leaveType, String leaveDateRange,Integer halfType, Integer dayCount, Integer employeeId, Boolean isNormal, String comment);

    Leave updateLeave(Integer id, Integer leaveType, String leaveDateRange,Integer halfType, Integer dayCount, String comment, Boolean isNormal);

    boolean deleteLeave(Integer id);
}
