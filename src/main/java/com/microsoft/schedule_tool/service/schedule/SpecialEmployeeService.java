package com.microsoft.schedule_tool.service.schedule;

import com.microsoft.schedule_tool.entity.schedule.SpecialEmployee;

import java.util.List;

/**
 * Created by Frank Hon on 11/26/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface SpecialEmployeeService {

    List<SpecialEmployee> getAllSpecialEmployees();

    SpecialEmployee saveSpecialEmployee(SpecialEmployee employee);

    boolean deleteSpecialEmployee(Integer id);
}
