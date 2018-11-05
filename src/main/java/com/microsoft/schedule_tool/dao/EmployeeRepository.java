package com.microsoft.schedule_tool.dao;

import com.microsoft.schedule_tool.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Frank Hon on 11/5/2018
 * E-mail: v-shhong@microsoft.com
 */

public interface EmployeeRepository extends JpaRepository<Employee,String> {

}
