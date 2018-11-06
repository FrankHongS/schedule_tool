package com.microsoft.schedule_tool.dao;

import com.microsoft.schedule_tool.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Frank Hon on 11/5/2018
 * E-mail: v-shhong@microsoft.com
 */

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    Optional<Employee> findByAlias(String alias);

    void deleteByAlias(String alias);
}
