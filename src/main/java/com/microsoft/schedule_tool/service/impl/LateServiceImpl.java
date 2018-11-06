package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.LateRepository;
import com.microsoft.schedule_tool.entity.LateType;
import com.microsoft.schedule_tool.service.LateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<LateType> getAllLatesByAlias(String alias) {
        return mLateRepository.findByAlias(alias);
    }

    @Transactional
    @Override
    public LateType saveLate(LateType late) {
        if (late.getName() == null || "".equals(late.getName()))
            throw new RuntimeException("name can't be null");
        if (late.getAlias() == null || "".equals(late.getAlias()))
            throw new RuntimeException("alias can't be null");
        if(late.getLateDate()==null)
            throw new RuntimeException("late date can't be null");

        try{
            LateType result=mLateRepository.save(late);

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
    public LateType updateLate(Integer id, String lateDate, String comment, Boolean isNormal) {
        if (!mLateRepository.findById(id).isPresent())
            throw new RuntimeException("late not existing, can't be updated...");

        try {
            LateType late=mLateRepository.findById(id).get();
            late.setLateDate(lateDate);
            late.setComment(comment);
            late.setNormal(isNormal);

            LateType result = mLateRepository.save(late);
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
