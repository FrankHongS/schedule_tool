package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.LateRepository;
import com.microsoft.schedule_tool.entity.Late;
import com.microsoft.schedule_tool.service.LateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public Late saveLate(Late late) {
        if (late.getName() == null || "".equals(late.getName()))
            throw new RuntimeException("name can't be null");
        if (late.getAlias() == null || "".equals(late.getAlias()))
            throw new RuntimeException("alias can't be null");
        if(late.getLateDate()==null)
            throw new RuntimeException("late date can't be null");

        try{
            Late result=mLateRepository.save(late);

            if(result!=null)
                return result;
            else
                throw new RuntimeException("fail to save late");
        }catch (Exception e){
            throw new RuntimeException("fail to save late "+e.getMessage());
        }
    }

    @Transactional
    @Override
    public Late updateLate(Integer id, Integer lateType, String lateDate, String comment, Boolean isNormal) {
        if (!mLateRepository.findById(id).isPresent())
            throw new RuntimeException("late not existing, can't be updated...");

        try {
            Late late=mLateRepository.findById(id).get();
            late.setLateType(lateType);
            late.setLateDate(lateDate);
            late.setComment(comment);
            late.setNormal(isNormal);

            Late result = mLateRepository.save(late);
            if (result != null)
                return result;
            else
                throw new RuntimeException("fail to update late");

        } catch (Exception e) {
            throw new RuntimeException("fail to update late " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean deleteLate(Integer id) {
        if(!mLateRepository.findById(id).isPresent())
            throw new RuntimeException("late id not existing");

        try{
            mLateRepository.deleteById(id);

            if(!mLateRepository.findById(id).isPresent())
                return true;
            else
                throw new RuntimeException("fail to delete late");

        }catch (Exception e){
            throw new RuntimeException("fail to delete late "+e.getMessage());
        }
    }
}
