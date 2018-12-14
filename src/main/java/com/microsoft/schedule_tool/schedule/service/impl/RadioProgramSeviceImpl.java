package com.microsoft.schedule_tool.schedule.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioStation;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqProgram;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RadioProgramsResp;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RoleResp;
import com.microsoft.schedule_tool.schedule.repository.RadioProgramRepository;
import com.microsoft.schedule_tool.schedule.repository.RadioStationRepository;
import com.microsoft.schedule_tool.schedule.service.RadioProgramService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Service
public class RadioProgramSeviceImpl implements RadioProgramService {
    @Autowired
    private RadioProgramRepository radioProgramRepository;

    @Autowired
    private RadioStationRepository radioStationRepository;


    @Override
    public long save(long stationId, String name) {
        Optional<RadioStation> stationOptional = radioStationRepository.findById(1L);
        if (!stationOptional.isPresent()) {
            throw new ProgramException(ResultEnum.STATION_NOT_EXTST);
        }
        RadioStation radioStation = stationOptional.get();

        if (name == null || "".equals(name)) {
            throw new ProgramException(ResultEnum.PROGRAM_NAME_NULL);
        }

        Optional<RadioProgram> programOptional = radioProgramRepository.findByNameAndRadioStation(name, radioStation);
        if (programOptional.isPresent()) {
            if (!programOptional.get().isDeleted()) {
                throw new ProgramException(ResultEnum.PROGRAM_NAME_EXIST);
            } else {
                try {
                    RadioProgram radioProgram = programOptional.get();
                    radioProgram.setDeleted(false);
                    radioProgramRepository.saveAndFlush(radioProgram);
                    return radioProgram.getId();
                } catch (Exception e) {
                    throw new ProgramException(ResultEnum.PROGRAM_SAVE_FAIL);
                }
            }
        }

        try {
            RadioProgram radioProgram = new RadioProgram();
            radioProgram.setName(name);
            radioProgram.setDeleted(false);
            radioProgram.setRadioStation(radioStation);
            RadioProgram save = radioProgramRepository.save(radioProgram);
            return save.getId();
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_SAVE_FAIL);
        }
    }

    @Override
    public void remove(long id) {
        Optional<RadioProgram> programOptional = radioProgramRepository.findById(id);
        if (!programOptional.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ID_NOT_EXIST);
        }

        try {
            RadioProgram radioProgram = programOptional.get();
            radioProgram.setDeleted(true);
            radioProgramRepository.saveAndFlush(radioProgram);
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_SAVE_FAIL);
        }
    }

    @Override
    public void changeName(long id, String newName) {
        Optional<RadioProgram> programOptional = radioProgramRepository.findById(id);
        if (!programOptional.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ID_NOT_EXIST);
        }

        if (newName == null || "".equals(newName)) {
            throw new ProgramException(ResultEnum.PROGRAM_NAME_NULL);
        }

        Optional<RadioProgram> byId = radioProgramRepository.findById(id);
        if (byId.isPresent()) {
            RadioStation radioStation = byId.get().getRadioStation();
            Optional<RadioProgram> optional = radioProgramRepository.findByNameAndRadioStation(newName, radioStation);
            if (optional.isPresent()) {
                throw new ProgramException(ResultEnum.PROGRAM_NAME_EXIST);
            }
        } else {
            throw new ProgramException(ResultEnum.PROGRAM_ID_NOT_EXIST);
        }

        try {
            RadioProgram radioProgram = programOptional.get();
            radioProgram.setName(newName);
            radioProgramRepository.saveAndFlush(radioProgram);
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_UPDATE_FAIL);
        }
    }

    @Override
    public long[] saveSome(String jsonData) {
        List<ReqProgram> programs = new ArrayList<>();
        try {
            ObjectMapper om = new ObjectMapper();
            JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, ReqProgram.class);
            programs = om.readValue(jsonData, javaType);
            if (programs.size() <= 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_WRONG_SAVE_PARAMS);
        }
        long[] re = new long[programs.size()];
        for (int i = 0; i < re.length; i++) {
            re[i] = save(programs.get(i).radioStationId, programs.get(i).programName);
        }
        return re;
    }

    @Override
    public List<RadioProgramsResp> findAllByStation(long stationId) {
        Optional<RadioStation> optional = radioStationRepository.findById(stationId);
        if (!optional.isPresent()) {
            throw new ProgramException(ResultEnum.STATION_NOT_EXTST);
        }
        try {
            List<RadioProgramsResp> resps = new ArrayList<>();
            List<RadioProgram> programs = radioProgramRepository.findAllByRadioStationAndIsDeleted(optional.get(), false);
            //处理stackoverflow
            for (RadioProgram program : programs) {
                RadioProgramsResp radioProgramsResp = new RadioProgramsResp();
                List<RoleResp> roleResps = new ArrayList<>();
                radioProgramsResp.id = program.getId();
                radioProgramsResp.name = program.getName();
                List<ProgramRole> programRoles = program.getProgramRoles();
                for (int i = 0; i < programRoles.size(); i++) {
                    RoleResp roleResp = new RoleResp();
                    ProgramRole programRole = programRoles.get(i);
                    roleResp.cycle = programRole.getCycle();
                    roleResp.id = programRole.getId();
                    roleResp.name = programRole.getName();
                    roleResp.workDays = programRole.getWorkDays();
                    roleResps.add(roleResp);
                }
                radioProgramsResp.programRoles = roleResps;
                resps.add(radioProgramsResp);
            }
            return resps;
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_FIND_FAILED);
        }
    }


}
