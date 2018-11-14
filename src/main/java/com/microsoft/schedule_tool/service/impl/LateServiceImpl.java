package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.LateRepository;
import com.microsoft.schedule_tool.entity.Late;
import com.microsoft.schedule_tool.service.LateService;
import com.microsoft.schedule_tool.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
@Service
public class LateServiceImpl implements LateService {

    @Autowired
    private LateRepository mLateRepository;

    @Override
    public List<Late> getAllLatesByEmployeeId(Integer employeeId) {
        return mLateRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Late> getAllLatesByEmployeeIdAndLateType(Integer employeeId, Integer LateType) {
        return mLateRepository.findByEmployeeIdAndLateType(employeeId, LateType);
    }

    @Override
    public List<Late> getAllLatesByDateRangeAndAlias(Date from, Date to, String alias) {
        return mLateRepository.findByCreatedTimeAfterAndCreatedTimeBeforeAndAlias(from, to, alias);
    }

    @Override
    public List<Late> getAllLatesByDateRange(Date from, Date to) {
        return mLateRepository.findByCreatedTimeAfterAndCreatedTimeBefore(from, to);
    }

    @Override
    public List<Late> getAllLatesByDateRangeAndEmployeeIdAndLeaveType(String from, String to, Integer employeeId, Integer lateType) {
        try {
            Date fromDate=DateUtil.parseDateString(from);
            Date toDate = new Date(DateUtil.parseDateString(to).getTime() + 24 * 60 * 60 * 1000);
            return mLateRepository.findByLateDateIsAfterAndLateDateBeforeAndEmployeeIdAndLateType(fromDate, toDate, employeeId,lateType);
        } catch (ParseException e) {
            throw new RuntimeException("query failed, date is not proper...");
        }
    }

    @Override
    public List<Late> getAllLatesByDateRangeAndEmployeeId(String from, String to, Integer employeeId) {
        try {
            Date fromDate=DateUtil.parseDateString(from);
            Date toDate = new Date(DateUtil.parseDateString(to).getTime() + 24 * 60 * 60 * 1000);
            return mLateRepository.findByLateDateIsAfterAndLateDateBeforeAndEmployeeId(fromDate, toDate, employeeId);
        } catch (ParseException e) {
            throw new RuntimeException("query failed, date is not proper...");
        }
    }

    @Transactional
    @Override
    public Late saveLate(Late late) {
        if (late.getName() == null || "".equals(late.getName()))
            throw new RuntimeException("name can't be null");
        if (late.getAlias() == null || "".equals(late.getAlias()))
            throw new RuntimeException("alias can't be null");
        if (late.getLateDate() == null)
            throw new RuntimeException("late date can't be null");

        try {
            Late result = mLateRepository.save(late);

            if (result != null) {
                return result;
            } else {
                throw new RuntimeException("fail to save late");
            }

        } catch (Exception e) {
            throw new RuntimeException("fail to save late " + e.getMessage());
        }
    }

    @Override
    public Late saveLate(String name, String alias, Integer lateType, String lateDate, Integer employeeId, Boolean isNormal, String comment) {
        if (name == null || "".equals(name))
            throw new RuntimeException("name can't be null");
        if (alias == null || "".equals(alias))
            throw new RuntimeException("alias can't be null");

        try {
            Late late = new Late();
            late.setName(name);
            late.setAlias(alias);
            late.setLateType(lateType);
            late.setLateDate(DateUtil.parseDateString(lateDate));
            late.setEmployeeId(employeeId);
            late.setNormal(isNormal);
            late.setComment(comment);

            Late result = mLateRepository.save(late);

            if (result != null) {
                return result;
            } else {
                throw new RuntimeException("fail to save late");
            }

        } catch (Exception e) {
            throw new RuntimeException("fail to save late,late date may be not proper " + e.getMessage());
        }

    }

    @Transactional
    @Override
    public Late updateLate(Integer id, Integer lateType, String lateDate, String comment, Boolean isNormal) {
        if (!mLateRepository.findById(id).isPresent())
            throw new RuntimeException("late not existing, can't be updated...");

        try {
            Late late = mLateRepository.findById(id).get();
            late.setLateType(lateType);
            late.setComment(comment);
            late.setNormal(isNormal);

            late.setLateDate(DateUtil.parseDateString(lateDate));

            Late result = mLateRepository.save(late);
            if (result != null)
                return result;
            else
                throw new RuntimeException("fail to update late");

        } catch (Exception e) {
            throw new RuntimeException("fail to save leave,leave date range may be not proper " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean deleteLate(Integer id) {
        if (!mLateRepository.findById(id).isPresent())
            throw new RuntimeException("late id not existing");

        try {
            mLateRepository.deleteById(id);

            if (!mLateRepository.findById(id).isPresent())
                return true;
            else
                throw new RuntimeException("fail to delete late");

        } catch (Exception e) {
            throw new RuntimeException("fail to delete late " + e.getMessage());
        }
    }
}
