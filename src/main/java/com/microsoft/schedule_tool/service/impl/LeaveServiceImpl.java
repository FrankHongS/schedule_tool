package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.Leave;
import com.microsoft.schedule_tool.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return mLeaveRepository.findByEmployeeIdAndLeaveType(employeeId,leaveType);
    }

    @Transactional
    @Override
    public Leave saveLeave(Leave leave) {
        if (leave.getName() == null || "".equals(leave.getName()))
            throw new RuntimeException("name can't be null");
        if (leave.getAlias() == null || "".equals(leave.getAlias()))
            throw new RuntimeException("alias can't be null");
        if(leave.getLeaveDateRange()==null || "".equals(leave.getLeaveDateRange()))
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
    public Leave updateLeave(Integer id, Integer leaveType,String leaveDateRange,Integer dayCount, String comment, Boolean isNormal) {
        if (!mLeaveRepository.findById(id).isPresent())
            throw new RuntimeException("leave not existing, can't be updated...");

        try {
            Leave leave=mLeaveRepository.findById(id).get();
            leave.setLeaveType(leaveType);
            leave.setLeaveDateRange(leaveDateRange);
            leave.setDayCount(dayCount);
            leave.setComment(comment);
            leave.setNormal(isNormal);

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

        if(!mLeaveRepository.findById(id).isPresent())
            throw new RuntimeException("leave id not existing");

        try{
            mLeaveRepository.deleteById(id);

            if(!mLeaveRepository.findById(id).isPresent())
                return true;
            else
                throw new RuntimeException("fail to delete leave");

        }catch (Exception e){
            throw new RuntimeException("fail to delete leave "+e.getMessage());
        }
    }
}
