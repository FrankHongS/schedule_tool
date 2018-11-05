package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.dao.EmployeeRepository;
import com.microsoft.schedule_tool.entity.Employee;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Frank Hon on 11/5/2018
 * E-mail: v-shhong@microsoft.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Ignore
    public void getAllEmployees() {
        List<Employee> employees=employeeRepository.findAll();

        assertEquals(1,employees.size());
    }

    @Test
    @Ignore
    public void saveEmployee() {
        Employee employee=new Employee();
        employee.setAlias("v-shhong");
        employee.setName("洪帅华");

        Employee result=employeeRepository.save(employee);
        assertNotNull(result);
    }

    @Test
    @Ignore
    public void updateEmployee(){
        String alias="v-shhong";
        String name="张三";
        boolean result=employeeRepository.findById(alias).isPresent();

        assertTrue(result);

        Employee employee=employeeRepository.findById(alias).get();
        employee.setName(name);
        Employee e=employeeRepository.save(employee);

        assertNotNull(e);
    }

    @Test
    public void deleteEmployee(){
        Employee e=new Employee();
        e.setAlias("v-shhong");

        if(employeeRepository.findById(e.getAlias()).isPresent())
            employeeRepository.delete(e);

        boolean result=employeeRepository.findById(e.getAlias()).isPresent();

        assertTrue(!result);
    }
}