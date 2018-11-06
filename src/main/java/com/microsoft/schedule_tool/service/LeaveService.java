package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.LeaveType;

import java.util.List;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */
public interface LeaveService {

    List<LeaveType> getAllLeavesByAlias(String alias);

    LeaveType saveLeave(LeaveType leave);

    LeaveType updateLeave(Integer id,String leaveDateRange, String comment,Boolean isNormal);

    boolean deleteLeave(Integer id);
}
