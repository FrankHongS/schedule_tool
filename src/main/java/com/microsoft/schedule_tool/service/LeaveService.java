package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.Leave;

import java.util.List;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */
public interface LeaveService {

    List<Leave> getAllLeavesByEmployeeId(Integer employeeId);

    List<Leave> getAllLeavesByEmployeeIdAndLeaveType(Integer employeeId,Integer leaveType);

    Leave saveLeave(Leave leave);

    Leave updateLeave(Integer id, Integer leaveType, String leaveDateRange,Integer dayCount, String comment, Boolean isNormal);

    boolean deleteLeave(Integer id);
}
