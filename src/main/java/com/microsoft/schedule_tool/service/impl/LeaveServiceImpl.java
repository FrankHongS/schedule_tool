package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.Leave;
import com.microsoft.schedule_tool.service.LeaveService;
import com.microsoft.schedule_tool.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepository mLeaveRepository;

    @Override
    public List<Leave> getAllLeavesByEmployeeId(Integer employeeId) {
        return mLeaveRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Leave> getAllLeavesByEmployeeIdAndLeaveType(Integer employeeId, Integer leaveType) {
        return mLeaveRepository.findByEmployeeIdAndLeaveType(employeeId, leaveType);
    }

    @Override
    public List<Leave> getAllLeavesByDateRangeAndAlias(Date from, Date to, String alias) {
        return mLeaveRepository.findByCreatedTimeAfterAndCreatedTimeBeforeAndAlias(from, to, alias);
    }

    @Override
    public List<Leave> getAllLeavesByDateRange(Date from, Date to) {
        return mLeaveRepository.findByCreatedTimeAfterAndCreatedTimeBefore(from, to);
    }

    @Override
    public List<Leave> getAllLeavesByDateRangeAndEmployeeIdAndLeaveType(String from, String to, Integer employeeId, Integer leaveType) {
        try {
            Date fromDate=DateUtil.parseDateString(from);
            Date toDate = new Date(DateUtil.parseDateString(to).getTime() + 24 * 60 * 60 * 1000);
            return mLeaveRepository.findByFromIsAfterAndFromBeforeAndEmployeeIdAndLeaveType(fromDate,toDate,employeeId,leaveType);
        } catch (ParseException e) {
            throw new RuntimeException("query failed, date range is not proper...");
        }
    }

    @Override
    public List<Leave> getAllLeavesByDateRangeAndEmployeeId(String from, String to, Integer employeeId) {

        try {
            Date fromDate=DateUtil.parseDateString(from);
            Date toDate = new Date(DateUtil.parseDateString(to).getTime() + 24 * 60 * 60 * 1000);
            return mLeaveRepository.findByFromIsAfterAndFromBeforeAndEmployeeId(fromDate, toDate, employeeId);
        } catch (ParseException e) {
            throw new RuntimeException("query failed, date range is not proper...");
        }

    }

    @Transactional
    @Override
    public Leave saveLeave(Leave leave) {
        if (leave.getName() == null || "".equals(leave.getName()))
            throw new RuntimeException("name can't be null");
        if (leave.getAlias() == null || "".equals(leave.getAlias()))
            throw new RuntimeException("alias can't be null");
        if (leave.getFrom() == null || leave.getTo() == null)
            throw new RuntimeException("leave date range can't be null");

        try {
            Leave result = mLeaveRepository.save(leave);
            if (result != null)
                return result;
            else
                throw new RuntimeException("fail to save leave");

        } catch (Exception e) {
            throw new RuntimeException("fail to save leave " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public Leave saveLeave(String name, String alias, Integer leaveType, String leaveDateRange,Integer halfType, Float dayCount, Integer employeeId, Boolean isNormal, String comment) {

        if (name == null || "".equals(name))
            throw new RuntimeException("name can't be null");
        if (alias == null || "".equals(alias))
            throw new RuntimeException("alias can't be null");

        try {
            Leave leave = new Leave();
            leave.setName(name);
            leave.setAlias(alias);
            leave.setLeaveType(leaveType);
            leave.setHalfType(halfType);
            leave.setDayCount(dayCount);
            leave.setEmployeeId(employeeId);
            leave.setNormal(isNormal);
            leave.setComment(comment);

            // convert dateString to date
            String[] dateArray = leaveDateRange.split(" - ");
            if (dateArray.length >= 2) {
                Date from = DateUtil.parseDateString(dateArray[0]);
                Date to = DateUtil.parseDateString(dateArray[1]);
                leave.setFrom(from);
                leave.setTo(to);

                Leave result = mLeaveRepository.save(leave);
                if (result != null) {
                    return result;
                } else {
                    throw new RuntimeException("fail to save leave");
                }

            } else {
                throw new RuntimeException("leave date range is not proper");
            }
        } catch (Exception e) {
            throw new RuntimeException("fail to save leave,leave date range may be not proper "+e.getMessage());
        }
    }

    @Transactional
    @Override
    public Leave updateLeave(Integer id, Integer leaveType, String leaveDateRange,Integer halfType, Float dayCount, String comment, Boolean isNormal) {
        if (!mLeaveRepository.findById(id).isPresent())
            throw new RuntimeException("leave not existing, can't be updated...");

        try {
            Leave leave = mLeaveRepository.findById(id).get();
            leave.setLeaveType(leaveType);
            leave.setHalfType(halfType);
            leave.setDayCount(dayCount);
            leave.setComment(comment);
            leave.setNormal(isNormal);

            String[] dateArray = leaveDateRange.split(" - ");
            if (dateArray.length >= 2) {
                Date from = DateUtil.parseDateString(dateArray[0]);
                Date to = DateUtil.parseDateString(dateArray[1]);
                leave.setFrom(from);
                leave.setTo(to);
            }else{
                throw new RuntimeException("leave date range is not proper");
            }

            Leave result = mLeaveRepository.save(leave);
            if (result != null)
                return result;
            else
                throw new RuntimeException("fail to update leave");

        } catch (Exception e) {
            throw new RuntimeException("fail to update leave " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean deleteLeave(Integer id) {

        if (!mLeaveRepository.findById(id).isPresent())
            throw new RuntimeException("leave id not existing");

        try {
            mLeaveRepository.deleteById(id);

            if (!mLeaveRepository.findById(id).isPresent())
                return true;
            else
                throw new RuntimeException("fail to delete leave");

        } catch (Exception e) {
            throw new RuntimeException("fail to delete leave " + e.getMessage());
        }
    }
}
