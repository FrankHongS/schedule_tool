package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.Leave;

import java.util.List;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */
public interface LeaveService {

    List<Leave> getAllLeavesByAlias(String alias);

    Leave saveLeave(Leave leave);

    Leave updateLeave(Integer id, Integer leaveType, String leaveDateRange, String comment, Boolean isNormal);

    boolean deleteLeave(Integer id);
}
