package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.EmployeeRepository;
import com.microsoft.schedule_tool.dao.LateRepository;
import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.Employee;
import com.microsoft.schedule_tool.entity.Late;
import com.microsoft.schedule_tool.entity.Leave;
import com.microsoft.schedule_tool.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private LeaveRepository mLeaveRepository;

    @Autowired
    private LateRepository mLateRepository;

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

    @Transactional
    @Override
    public Employee updateEmployee(Integer id,String alias,String name) {

        if(mEmployeeRepository.findById(id).isPresent()){
            Employee employee=mEmployeeRepository.findById(id).get();

            employee.setName(name);
            employee.setAlias(alias);

            // update tb-leave
            List<Leave> leaves=mLeaveRepository.findByEmployeeId(id);
            for(Leave leave:leaves){
                leave.setName(name);
                leave.setAlias(alias);
                mLeaveRepository.save(leave);
            }

            // update tb-late
            List<Late> lates=mLateRepository.findByEmployeeId(id);
            for(Late late:lates){
                late.setName(name);
                late.setAlias(alias);
                mLateRepository.save(late);
            }

            try{
                mEmployeeRepository.save(employee);

                return employee;
            }catch (Exception e){
                throw new RuntimeException("fail to save employee "+e.getMessage());
            }

        }else{
            throw new RuntimeException("employee not existing,can't be updated");
        }

    }

    @Transactional
    @Override
    public boolean deleteEmployee(Integer id) {

        if(!mEmployeeRepository.findById(id).isPresent())
            throw new RuntimeException("employee id not existing");

        try {
            mEmployeeRepository.deleteById(id);

            if(mEmployeeRepository.findById(id).isPresent())
                throw new RuntimeException("fail to delete employee");
        }catch (Exception e){
            throw new RuntimeException("fail to delete employee "+e.getMessage());
        }

        try {
            mLeaveRepository.deleteByEmployeeId(id);

            if(mLeaveRepository.findByEmployeeId(id).size()>0)
                throw new RuntimeException("fail to delete employee");
        }catch (Exception e){
            throw new RuntimeException("fail to delete employee "+e.getMessage());
        }

        try {
            mLateRepository.deleteByEmployeeId(id);

            if(mLateRepository.findByEmployeeId(id).size()>0)
                throw new RuntimeException("fail to delete employee");
        }catch (Exception e){
            throw new RuntimeException("fail to delete employee "+e.getMessage());
        }

        return true;
    }

    @Override
    public Page<Employee> getEmployeesByPageNoCriteria(Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size, Sort.Direction.ASC,"id");
        return mEmployeeRepository.findAll(pageable);
    }
}
