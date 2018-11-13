package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.EmployeeRepository;
import com.microsoft.schedule_tool.dao.LateRepository;
import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.Employee;
import com.microsoft.schedule_tool.entity.Late;
import com.microsoft.schedule_tool.entity.Leave;
import com.microsoft.schedule_tool.service.ExcelService;
import com.microsoft.schedule_tool.util.Constants;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.util.Util;
import com.microsoft.schedule_tool.vo.leavesum.LeaveYearSum;
import com.microsoft.schedule_tool.vo.leavesum.YearSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 *
 * sum of Leave and Late
 */
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private EmployeeRepository mEmployeeRepository;

    @Autowired
    private LeaveRepository mLeaveRepository;

    @Autowired
    private LateRepository mLateRepository;

    @Override
    public List<YearSum> getYearSum(String year) {

        try {
            String from=year+"-01-01";
            String to=year+"-12-31";
            Date fromDate= DateUtil.parseDateString(from);
            Date toDate=new Date(DateUtil.parseDateString(to).getTime() + 24 * 60 * 60 * 1000);

            List<YearSum> yearSumList=new ArrayList<>();

            List<Employee> employeeList=mEmployeeRepository.findAll();

            for(Employee employee:employeeList){
                YearSum yearSum=generateYearSum(year,fromDate,toDate,employee.getId(),employee.getName());
                yearSumList.add(yearSum);
            }

            return yearSumList;
        } catch (ParseException e) {
            throw new RuntimeException("year format is not proper...");
        }

    }

    private YearSum generateYearSum(String year, Date fromDate, Date toDate, Integer employeeId, String name) throws ParseException {

        List<Integer> allYearSum=new ArrayList<>();

        for(int i = 0; i<Constants.LEAVE.length+Constants.LATE.length;i++){
            int count;

            if(i<Constants.LEAVE.length){

                List<Leave> leaveList=mLeaveRepository.findByFromAfterAndFromBeforeAndEmployeeIdAndLeaveType(
                        fromDate,toDate,employeeId,i);

                count=Util.getLeaveDayCount(leaveList);
            }else{
                List<Late> lateList=mLateRepository.findByLateDateAfterAndLateDateBeforeAndEmployeeIdAndLateType(
                        fromDate,toDate,employeeId,i-9
                );

                count=lateList.size();
            }

            allYearSum.add(count);
        }

        YearSum yearSum=new YearSum();
        yearSum.setName(name);
        yearSum.setYearSum(allYearSum);

        List<List<Integer>> monthSum=new ArrayList<>();
        for(int j=0;j<12;j++){
            monthSum.add(generateMonthSum(year+"-0"+(j+1)+"-01",year+"-0"+(j+2)+"-01",employeeId));
        }

        yearSum.setMonthSum(monthSum);

        return yearSum;
    }

    private List<Integer> generateMonthSum(String from,String to,Integer employeeId) throws ParseException {

        Date fromDate= DateUtil.parseDateString(from);
        Date toDate=DateUtil.parseDateString(to);

        List<Integer> monthSum=new ArrayList<>();

        for(int i = 0; i<Constants.LEAVE.length+Constants.LATE.length;i++){
            int count;

            if(i<Constants.LEAVE.length){

                List<Leave> leaveList=mLeaveRepository.findByFromAfterAndFromBeforeAndEmployeeIdAndLeaveType(
                        fromDate,toDate,employeeId,i);

                count=Util.getLeaveDayCount(leaveList);
            }else{
                List<Late> lateList=mLateRepository.findByLateDateAfterAndLateDateBeforeAndEmployeeIdAndLateType(
                        fromDate,toDate,employeeId,i-9
                );

                count=lateList.size();
            }

            monthSum.add(count);
        }

        return monthSum;
    }

}
