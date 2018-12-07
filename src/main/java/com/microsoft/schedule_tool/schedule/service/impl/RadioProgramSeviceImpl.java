package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioStation;
import com.microsoft.schedule_tool.schedule.repository.RadioStationRepository;
import com.microsoft.schedule_tool.schedule.service.RadioProgramService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
public class RadioProgramSeviceImpl implements RadioProgramService {
    @Autowired
    private RadioStationRepository radioStationRepository;


    @Override
    public long save(long stationId, String name) {
        
        return 0;
    }

    @Override
    public void remove(long id) {

    }

    @Override
    public void changeName(long id, String newName) {

    }

    @Override
    public long[] saveSome(String jsonData) {
        return new long[0];
    }

    @Override
    public List<RadioStation> findAll(long stationId) {
        return null;
    }
}
