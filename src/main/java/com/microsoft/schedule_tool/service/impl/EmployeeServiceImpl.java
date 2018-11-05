package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.EmployeeRepository;
import com.microsoft.schedule_tool.entity.Employee;
import com.microsoft.schedule_tool.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/5/2018
 * E-mail: v-shhong@microsoft.com
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository mEmployeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> result=mEmployeeRepository.findAll();
        return result;
    }

    @Transactional
    @Override
    public Employee saveEmployee(Employee employee) {
        if(employee==null)
            throw new RuntimeException("employee can't be null");
        if(employee.getAlias()==null||"".equals(employee.getAlias()))
            throw new RuntimeException("employee alias can't be null");
        if (employee.getName()==null||"".equals(employee.getName()))
            throw new RuntimeException("employee name can't be null");

        try {
            Employee result=mEmployeeRepository.save(employee);
            if (result!=null){
                return result;
            } else
                throw new RuntimeException("fail to save employee");
        }catch (Exception e){
            throw new RuntimeException("fail to save employee "+e.getMessage());
        }
    }

    @Override
    public boolean updateEmployee(String alias) {
        return false;
    }

    @Transactional
    @Override
    public boolean deleteEmployee(String alias) {

        if(!mEmployeeRepository.findByAlias(alias).isPresent())
            throw new RuntimeException("employee alias not existing");
        try {
            mEmployeeRepository.deleteById(alias);

            if(!mEmployeeRepository.findByAlias(alias).isPresent())
                return true;
            else
                throw new RuntimeException("fail to delete employee");
        }catch (Exception e){
            throw new RuntimeException("fail to delete employee "+e.getMessage());
        }
    }
}
