package com.microsoft.schedule_tool.service.impl;

import com.microsoft.schedule_tool.dao.HomebaseRepository;
import com.microsoft.schedule_tool.entity.HomebaseType;
import com.microsoft.schedule_tool.entity.LateType;
import com.microsoft.schedule_tool.service.HomebaseService;
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
public class HomebaseServiceImpl implements HomebaseService {

    @Autowired
    private HomebaseRepository mHomebaseRepository;

    @Override
    public List<HomebaseType> getAllHomebasesByAlias(String alias) {
        return mHomebaseRepository.findByAlias(alias);
    }

    @Transactional
    @Override
    public HomebaseType saveHomebase(HomebaseType homebase) {
        if (homebase.getName() == null || "".equals(homebase.getName()))
            throw new RuntimeException("name can't be null");
        if (homebase.getAlias() == null || "".equals(homebase.getAlias()))
            throw new RuntimeException("alias can't be null");
        if(homebase.getHomebaseDate()==null)
            throw new RuntimeException("homebase date can't be null");

        try{
            HomebaseType result=mHomebaseRepository.save(homebase);

            if(result!=null)
                return result;
            else
                throw new RuntimeException("fail to save homebase");
        }catch (Exception e){
            throw new RuntimeException("fail to save homebase "+e.getMessage());
        }
    }

    @Transactional
    @Override
    public HomebaseType updateHomebase(Integer id, String homebaseDate, String comment, Boolean isNormal) {
        if (!mHomebaseRepository.findById(id).isPresent())
            throw new RuntimeException("homebase not existing, can't be updated...");

        try {
            HomebaseType homebase=mHomebaseRepository.findById(id).get();
            homebase.setHomebaseDate(homebaseDate);
            homebase.setComment(comment);
            homebase.setNormal(isNormal);

            HomebaseType result = mHomebaseRepository.save(homebase);
            if (result != null)
                return result;
            else
                throw new RuntimeException("fail to update homebase");

        } catch (Exception e) {
            throw new RuntimeException("fail to update homebase " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean deleteHomebase(Integer id) {
        if(!mHomebaseRepository.findById(id).isPresent())
            throw new RuntimeException("homebase id not existing");

        try{
            mHomebaseRepository.deleteById(id);

            if(!mHomebaseRepository.findById(id).isPresent())
                return true;
            else
                throw new RuntimeException("fail to delete homebase");

        }catch (Exception e){
            throw new RuntimeException("fail to delete homebase "+e.getMessage());
        }
    }
}
