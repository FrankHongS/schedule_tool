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
import com.microsoft.schedule_tool.vo.excel.MonthDetailSum;
import com.microsoft.schedule_tool.vo.excel.YearSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

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

    @Override
    public List<MonthDetailSum> getMonthDetailSum(String month) {
        try {
            String from=month+"-01";
            String to=DateUtil.parseMonthString(month);
            Date fromDate= new Date(DateUtil.parseDateString(from).getTime() - 24 * 60 * 60 * 1000);
            Date toDate=DateUtil.parseDateString(to);

            List<MonthDetailSum> monthDetailSumList =new ArrayList<>();

            List<Employee> employeeList=mEmployeeRepository.findAll();
            for(Employee employee:employeeList){
                MonthDetailSum monthDetailSum=
                        generateMonthDetailSum(fromDate,toDate,employee.getId(),employee.getName());
                monthDetailSum.setMonth(month);
                monthDetailSumList.add(monthDetailSum);
            }

            return monthDetailSumList;
        } catch (ParseException e) {
            throw new RuntimeException("month format is not proper...");
        }
    }

    private YearSum generateYearSum(String year, Date fromDate, Date toDate, Integer employeeId, String name) throws ParseException {

        List<Float> allYearSum=new ArrayList<>();

        for(int i = 0; i<Constants.LEAVE.length+Constants.LATE.length;i++){
            Float count;

            if(i<Constants.LEAVE.length){

                List<Leave> leaveList=mLeaveRepository.findByFromIsAfterAndFromBeforeAndEmployeeIdAndLeaveType(
                        fromDate,toDate,employeeId,i);

                count=Util.getLeaveDayCount(leaveList);
            }else{
                List<Late> lateList=mLateRepository.findByLateDateIsAfterAndLateDateBeforeAndEmployeeIdAndLateType(
                        fromDate,toDate,employeeId,i-9
                );

                count= (float) lateList.size();
            }

            allYearSum.add(count);
        }

        YearSum yearSum=new YearSum();
        yearSum.setName(name);
        yearSum.setYearSum(allYearSum);

        List<List<Float>> monthSum=new ArrayList<>();
        for(int j=0;j<12;j++){
            monthSum.add(generateMonthSum(year+"-0"+(j+1)+"-01",year+"-0"+(j+2)+"-01",employeeId));
        }

        yearSum.setMonthSum(monthSum);

        return yearSum;
    }

    private List<Float> generateMonthSum(String from,String to,Integer employeeId) throws ParseException {

        Date fromDate= DateUtil.parseDateString(from);
        Date toDate=DateUtil.parseDateString(to);

        List<Float> monthSum=new ArrayList<>();

        for(int i = 0; i<Constants.LEAVE.length+Constants.LATE.length;i++){
            Float count;

            if(i<Constants.LEAVE.length){

                List<Leave> leaveList=mLeaveRepository.findByFromIsAfterAndFromBeforeAndEmployeeIdAndLeaveType(
                        fromDate,toDate,employeeId,i);

                count=Util.getLeaveDayCount(leaveList);
            }else{
                List<Late> lateList=mLateRepository.findByLateDateIsAfterAndLateDateBeforeAndEmployeeIdAndLateType(
                        fromDate,toDate,employeeId,i-9
                );

                count= (float) lateList.size();
            }

            monthSum.add(count);
        }

        return monthSum;
    }

    private MonthDetailSum generateMonthDetailSum(Date fromDate, Date toDate, Integer employeeId, String name){

        Map<Long,String> descMap=new HashMap<>();

        List<Leave> leaveList=mLeaveRepository.findByFromIsAfterAndFromBeforeAndEmployeeId(fromDate,toDate,employeeId);
        for(Leave leave:leaveList){
            String desc=Util.getLeaveDesc(leave);
            descMap.put(leave.getFrom().getTime(),desc);
        }

        List<Late> lateList=mLateRepository.findByLateDateIsAfterAndLateDateBeforeAndEmployeeId(fromDate,toDate,employeeId);
        for(Late late:lateList){
            String desc=Util.getLateDesc(late);
            long time=late.getLateDate().getTime();
            if(descMap.containsKey(time)){
                descMap.put(time,descMap.get(time)+"  "+desc);
            }else{
                descMap.put(time,desc);
            }
        }

        MonthDetailSum monthDetailSum=new MonthDetailSum();
        monthDetailSum.setName(name);
        monthDetailSum.setDescMap(descMap);

        return monthDetailSum;
    }

}
