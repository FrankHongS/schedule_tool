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
    private LeaveRepository leaveRepository;

    @Override
    public List<LeaveType> getAllLeavesByAlias(String alias) {
        return leaveRepository.findByAlias(alias);
    }

    @Transactional
    @Override
    public boolean saveLeave(LeaveType leave) {
        if (leave.getName() == null || "".equals(leave.getName()))
            throw new RuntimeException("姓名不能为空！");
        if (leave.getAlias() == null || "".equals(leave.getAlias()))
            throw new RuntimeException("alias不能为空！");

        try {
            LeaveType result = leaveRepository.save(leave);
            if (result != null)
                return true;
            else
                throw new RuntimeException("保存请假失败");

        } catch (Exception e) {
            throw new RuntimeException("保存请假失败 " + e.getMessage());
        }
    }

    @Override
    public boolean updateLeave(Integer id, String comment,Boolean isNormal) {
        if (!leaveRepository.findById(id).isPresent())
            throw new RuntimeException("该项不存在，无法更新...");

        try {
            LeaveType leave=leaveRepository.findById(id).get();
            leave.setComment(comment);
            leave.setNormal(isNormal);

            LeaveType result = leaveRepository.save(leave);
            if (result != null)
                return true;
            else
                throw new RuntimeException("更新请假失败");

        } catch (Exception e) {
            throw new RuntimeException("更新请假失败 " + e.getMessage());
        }
    }
}
