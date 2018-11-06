package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.Employee;

import java.util.List;

/**
 * Created by Frank Hon on 11/5/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee saveEmployee(Employee employee);

    Employee updateEmployee(Integer id, String alias, String name);

    boolean deleteEmployee(String alias);
}
