package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.LeaveType;
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
    public List<LeaveType> getAllLeavesByAlias(String alias) {
        return mLeaveRepository.findByAlias(alias);
    }

    @Transactional
    @Override
    public LeaveType saveLeave(LeaveType leave) {
        if (leave.getName() == null || "".equals(leave.getName()))
            throw new RuntimeException("name can't be null");
        if (leave.getAlias() == null || "".equals(leave.getAlias()))
            throw new RuntimeException("alias can't be null");
        if(leave.getLeaveDateRange()==null || "".equals(leave.getLeaveDateRange()))
            throw new RuntimeException("leave date range can't be null");

        try {
            LeaveType result = mLeaveRepository.save(leave);
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
    public LeaveType updateLeave(Integer id, String leaveDateRange, String comment,Boolean isNormal) {
        if (!mLeaveRepository.findById(id).isPresent())
            throw new RuntimeException("leave not existing, can't be updated...");

        try {
            LeaveType leave=mLeaveRepository.findById(id).get();
            leave.setLeaveDateRange(leaveDateRange);
            leave.setComment(comment);
            leave.setNormal(isNormal);

            LeaveType result = mLeaveRepository.save(leave);
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
