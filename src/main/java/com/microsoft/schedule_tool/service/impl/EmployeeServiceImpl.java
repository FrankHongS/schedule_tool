package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.EmployeeRepository;
import com.microsoft.schedule_tool.dao.LateRepository;
import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.Employee;
import com.microsoft.schedule_tool.entity.Late;
import com.microsoft.schedule_tool.entity.Leave;
import com.microsoft.schedule_tool.exception.EmployeeException;
import com.microsoft.schedule_tool.service.EmployeeService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
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
        return mEmployeeRepository.findAll();
    }

    @Transactional
    @Override
    public Employee saveEmployee(Employee employee) {
        if(employee.getAlias()==null||"".equals(employee.getAlias()))
            throw new EmployeeException(ResultEnum.EMPLOYEE_ALIAS_NULL);
        if (employee.getName()==null||"".equals(employee.getName()))
            throw new EmployeeException(ResultEnum.EMPLOYEE_NAME_NULL);

        if(mEmployeeRepository.findByAlias(employee.getAlias()).isPresent()){
            throw new EmployeeException(ResultEnum.EMPLOYEE_ALIAS_EXIST);
        }

        try {
            Employee result=mEmployeeRepository.save(employee);
            if (result!=null){
                return result;
            } else
                throw new EmployeeException(ResultEnum.EMPLOYEE_SAVE_FAIL);
        }catch (Exception e){
            throw new EmployeeException(ResultEnum.EMPLOYEE_SAVE_FAIL);
        }
    }

    @Transactional
    @Override
    public Employee updateEmployee(Integer id,String alias,String name,Float annual) {

        if(mEmployeeRepository.findById(id).isPresent()){
            Employee employee=mEmployeeRepository.findById(id).get();

            employee.setName(name);
            employee.setAlias(alias);
            employee.setAnnual(annual);

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
                throw new EmployeeException(ResultEnum.EMPLOYEE_UPDATE_FAIL);
            }

        }else{
            throw new EmployeeException(ResultEnum.EMPLOYEE_NOT_EXIST);
        }

    }

    @Transactional
    @Override
    public boolean deleteEmployee(Integer id) {

        if(!mEmployeeRepository.findById(id).isPresent())
            throw new EmployeeException(ResultEnum.EMPLOYEE_ID_NOT_EXIST);

        try {
            mEmployeeRepository.deleteById(id);

            if(mEmployeeRepository.findById(id).isPresent())
                throw new EmployeeException(ResultEnum.EMPLOYEE_DELETE_FAIL);
        }catch (Exception e){
            throw new EmployeeException(ResultEnum.EMPLOYEE_DELETE_FAIL);
        }

        try {
            mLeaveRepository.deleteByEmployeeId(id);

            if(mLeaveRepository.findByEmployeeId(id).size()>0)
                throw new EmployeeException(ResultEnum.EMPLOYEE_DELETE_FAIL);
        }catch (Exception e){
            throw new EmployeeException(ResultEnum.EMPLOYEE_DELETE_FAIL);
        }

        try {
            mLateRepository.deleteByEmployeeId(id);

            if(mLateRepository.findByEmployeeId(id).size()>0)
                throw new EmployeeException(ResultEnum.EMPLOYEE_DELETE_FAIL);
        }catch (Exception e){
            throw new EmployeeException(ResultEnum.EMPLOYEE_DELETE_FAIL);
        }

        return true;
    }

    @Override
    public Page<Employee> getEmployeesByPageNoCriteria(Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size, Sort.Direction.ASC,"id");
        return mEmployeeRepository.findAll(pageable);
    }
}
