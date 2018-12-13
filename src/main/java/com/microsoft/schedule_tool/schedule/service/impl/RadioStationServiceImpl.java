package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioStation;
import com.microsoft.schedule_tool.schedule.repository.RadioStationRepository;
import com.microsoft.schedule_tool.schedule.service.RadioStationService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Service
public class RadioStationServiceImpl implements RadioStationService {
    @Autowired
    private RadioStationRepository radioStationRepository;

    @Override
    public void init() {
        try {
            Optional<RadioStation> byId = radioStationRepository.findById(1l);
            if (!byId.isPresent()) {
                RadioStation radioStation = new RadioStation();
                radioStation.setDeleted(false);
                radioStation.setName("test");
                radioStation.setId(1l);
                radioStation.setRadioProgramSet(null);
                radioStationRepository.save(radioStation);
            }
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.STATION_NOT_EXTST);
        }
    }
}
