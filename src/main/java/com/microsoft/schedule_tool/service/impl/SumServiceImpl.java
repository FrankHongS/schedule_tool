package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.EmployeeRepository;
import com.microsoft.schedule_tool.dao.LateRepository;
import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.Employee;
import com.microsoft.schedule_tool.entity.Leave;
import com.microsoft.schedule_tool.service.SumService;
import com.microsoft.schedule_tool.vo.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */

@Service
public class SumServiceImpl implements SumService {

    @Autowired
    private EmployeeRepository mEmployeeRepository;

    @Autowired
    private LeaveRepository mLeaveRepository;

    @Autowired
    private LateRepository mLateRepository;


    @Override
    public List<Attendance> getSumOfAllTypes() {

        List<Attendance> target=new ArrayList<>();

        List<Employee> employees=mEmployeeRepository.findAll();

        for (Employee employee : employees) {

            Attendance attendance=new Attendance();

            attendance.setEmployeeId(employee.getId());
            attendance.setName(employee.getName());
            String alias=employee.getAlias();
            attendance.setAlias(alias);

            attendance.setLeaveSum(getLeaveDayCount(mLeaveRepository.findByAlias(alias)));
            attendance.setLateSum(mLateRepository.findByAlias(alias).size());

            target.add(attendance);
        }

        return target;
    }

    @Override
    public List<Attendance> getSumOfAllTypesByAlias(String alias) {
        List<Attendance> target=new ArrayList<>();

        if(mEmployeeRepository.findByAlias(alias).isPresent()){

            Attendance attendance=new Attendance();
            Employee employee=mEmployeeRepository.findByAlias(alias).get();
            attendance.setEmployeeId(employee.getId());
            attendance.setName(employee.getName());
            attendance.setAlias(alias);
            attendance.setLeaveSum(mLeaveRepository.findByAlias(alias).size());
            attendance.setLateSum(mLateRepository.findByAlias(alias).size());

            target.add(attendance);
        }else{
            throw new RuntimeException("alias not existing");
        }

        return target;
    }

    private int getLeaveDayCount(List<Leave> leaveList){
        int sum=0;
        for(Leave leave:leaveList){
            sum+=leave.getDayCount();
        }

        return sum;
    }
}
