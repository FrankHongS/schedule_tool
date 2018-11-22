package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.Late;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface LateService {

    List<Late> getAllLatesByEmployeeId(Integer employeeId);

    List<Late> getAllLatesByEmployeeIdAndLateType(Integer employeeId,Integer LateType);

    List<Late> getAllLatesByDateRangeAndAlias(Date from, Date to, String alias);

    List<Late> getAllLatesByDateRange(Date from, Date to);

    List<Late> getAllLatesByDateRangeAndEmployeeIdAndLeaveType(String from, String to, Integer employeeId, Integer lateType);

    List<Late> getAllLatesByDateRangeAndEmployeeId(String from, String to, Integer employeeId);

    List<Late> getAllLatesOrderByCreatedTime(Integer page,Integer size);

    Late saveLate(Late late);

    Late saveLate(String name, String alias, Integer lateType, String lateDate, Integer employeeId, Boolean isNormal, String comment);

    Late updateLate(Integer id, Integer lateType, String lateDate, String comment, Boolean isNormal);

    boolean deleteLate(Integer id);
}
