package com.microsoft.schedule_tool.schedule.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.schedule_tool.entity.schedule.Program;
import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqProgram;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqRole;
import com.microsoft.schedule_tool.schedule.repository.ProgramRoleRepository;
import com.microsoft.schedule_tool.schedule.repository.RadioProgramRepository;
import com.microsoft.schedule_tool.schedule.service.ProgramRoleService;
import com.microsoft.schedule_tool.util.StringUtils;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
@Service
public class ProgramRoleServiceImpl implements ProgramRoleService {

    @Autowired
    private RadioProgramRepository radioProgramRepository;

    @Autowired
    private ProgramRoleRepository programRoleRepository;

    @Override
    public long save(long programId, String name, String scheduleCycle, int workDays) {
        //1:check params
        Optional<RadioProgram> radioProgramOptional = radioProgramRepository.findById(programId);
        if (!radioProgramOptional.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ID_NOT_EXIST);
        }

        if (StringUtils.isEmpty(name)) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_NAME_IS_NULL);
        }
        //check repeat name;
        List<ProgramRole> byNameAndRadioProgram = programRoleRepository.findByNameAndRadioProgram(name, radioProgramOptional.get());
        if (byNameAndRadioProgram.size()>1) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_NAME_REPEAT);
        }
        if (StringUtils.isEmpty(scheduleCycle) || scheduleCycle.length() != 7) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_WRONG_CYCLE);
        }

        if (workDays <= 0) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_WRONG_WORKDAYS);
        }
        //2:add
        try {
            ProgramRole programRole = new ProgramRole();
            programRole.setCycle(scheduleCycle);
            programRole.setDeleted(false);
            programRole.setName(name);
            programRole.setRadioProgram(radioProgramOptional.get());
            programRole.setWorkDays(workDays);
            ProgramRole save = programRoleRepository.save(programRole);
            //3:return id
            return save.getId();
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_SAVE_FAILED);
        }
    }

    @Override
    public void remove(long id) {
        //1:check params
        Optional<ProgramRole> byId = programRoleRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_ID_NOT_EXIST);
        }
        ProgramRole programRole = byId.get();
        //2:change deleted;
        programRole.setDeleted(true);
        //3:save
        try {
            programRoleRepository.saveAndFlush(programRole);
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_REMOVE_FAILED);
        }
    }

    @Override
    public void update(long id, String name, String scheduleCycle, int workDays) {
        //1:check params：
        Optional<ProgramRole> byId = programRoleRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_ID_NOT_EXIST);
        }

        if (StringUtils.isEmpty(name)) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_NAME_IS_NULL);
        }
        //check repeat name;
        RadioProgram radioProgram = byId.get().getRadioProgram();
        List<ProgramRole> byNameAndRadioProgram = programRoleRepository.findByNameAndRadioProgram(name, radioProgram);

        List<ProgramRole> temp=new ArrayList<>();

        for(ProgramRole role:byNameAndRadioProgram){
            if(!role.isDeleted()){
                temp.add(role);
            }
        }

        if (temp.size()>1) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_NAME_REPEAT);
        }
        if (StringUtils.isEmpty(scheduleCycle) || scheduleCycle.length() != 7) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_WRONG_CYCLE);
        }

        if (workDays <= 0) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_WRONG_WORKDAYS);
        }

        try {
            //2:change msg
            ProgramRole programRole = byId.get();
            programRole.setWorkDays(workDays);
            programRole.setName(name);
            programRole.setCycle(scheduleCycle);
            //3:save
            programRoleRepository.saveAndFlush(programRole);
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_UPDATE_FAILED);
        }
    }

    @Override
    public long[] saveSome(String jsonData) {
        List<ReqRole> roles = new ArrayList<>();
        try {
            ObjectMapper om = new ObjectMapper();
            JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, ReqRole.class);
            roles = om.readValue(jsonData, javaType);
            if (roles.size() <= 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_WRONG_SAVE_PARAMS);
        }
        long[] re = new long[roles.size()];
        for (int i = 0; i < re.length; i++) {
            re[i] = save(roles.get(i).programId, roles.get(i).name, roles.get(i).cycle, roles.get(i).workDays);
        }
        return re;
    }

    @Override
    public List<ProgramRole> findAllByProgram(long programId) {
        //1:check params
        Optional<RadioProgram> byId = radioProgramRepository.findById(programId);
        if (!byId.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ID_NOT_EXIST);
        }
        try {
            //2:find (not deleted,  programId)
            List<ProgramRole> programRoles = programRoleRepository.findAllByRadioProgramAndIsDeleted(byId.get(), false);
            for (ProgramRole role :
                    programRoles) {
                role.setRadioProgram(null);
            }
            //3:return roles
            return programRoles;
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_FIND_FAILED);
        }
    }
}
