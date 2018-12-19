package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.entity.schedule.Program;
import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.EqualRole;
import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RelationRoleAndEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.response.EquelRoleResp;
import com.microsoft.schedule_tool.schedule.repository.EqualRolesResposity;
import com.microsoft.schedule_tool.schedule.repository.ProgramRoleRepository;
import com.microsoft.schedule_tool.schedule.repository.RelationRoleAndEmployeeRepository;
import com.microsoft.schedule_tool.schedule.service.EqualRolesService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/17
 **/
@Service
public class EqualRolesServiceImpl implements EqualRolesService {
    @Autowired
    private EqualRolesResposity equalRolesResposity;

    @Autowired
    private ProgramRoleRepository programRoleRepository;

    @Autowired
    private RelationRoleAndEmployeeRepository relationRoleAndEmployeeRepository;

    @Override
    public List<EquelRoleResp> getAllEqualRolesGroup() {
        try {
            List<EqualRole> all = equalRolesResposity.findAll();
            List<EquelRoleResp> re = new ArrayList<>();
            for (int i = 0; i < all.size(); i++) {
                EquelRoleResp equelRoleResp = new EquelRoleResp();
                equelRoleResp.id = all.get(i).getId();
                List<EquelRoleResp.Roles> roles = new ArrayList<>();
                String ids = all.get(i).getIds();
                String[] split = ids.split(",");
                for (int j = 0; j < split.length; j++) {
                    EquelRoleResp.Roles item = new EquelRoleResp.Roles();
                    Long id = Long.valueOf(split[j]);
                    item.id = id;
                    Optional<ProgramRole> byId = programRoleRepository.findById(id);
                    if (byId.isPresent()) {
                        item.programName = byId.get().getRadioProgram().getName();
                        item.roleName = byId.get().getName();
                    }
                    roles.add(item);
                }
                equelRoleResp.rolesList = roles;
                re.add(equelRoleResp);
            }
            return re;
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_GET_ERROR);
        }
    }

    @Override
    public void addEqualRoles(String ids) {
        if (ids == null) {
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_IDS_NULL);
        }
        List<EqualRole> all = equalRolesResposity.findAll();
        for (int i = 0; i < all.size(); i++) {
            String ids1 = all.get(i).getIds();
            if (ids.equals(ids1)) {
                return;
            }
        }

        String[] split = ids.split(",");
        if (split.length <= 1) {
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_IDS_NUMBER_MUST_BIG_THAN2);
        }
        //要求每个role下的employeeIds相同，ratio相同，否则报异常
        List<RelationRoleAndEmployee> allByRoleId = relationRoleAndEmployeeRepository.getAllByRoleId(Long.valueOf(split[0]));
        for (int i = 1; i < split.length; i++) {
            List<RelationRoleAndEmployee> allByRoleId1 = relationRoleAndEmployeeRepository.getAllByRoleId(Long.valueOf(split[i]));
            if (!isEquels(allByRoleId, allByRoleId1)) {
                throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_MUST_HAVE_SAME_EMPLOYEES);
            }
        }
        try {
            EqualRole equalRole = new EqualRole();
            equalRole.setIds(ids);
            equalRolesResposity.save(equalRole);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_SAVE_FAILED);
        }
    }

    private boolean isEquels(List<RelationRoleAndEmployee> listA, List<RelationRoleAndEmployee> listB) {
        if (listA.size() != listB.size()) {
            return false;
        }
        for (int i = 0; i < listA.size(); i++) {
            int ratio = listA.get(i).getRatio();
            Long employeeId = listA.get(i).getEmployeeId();

            boolean has = false;
            for (int j = 0; j < listB.size(); j++) {
                if (ratio == listB.get(j).getRatio() &&
                        employeeId.equals(listB.get(j).getEmployeeId())) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void updateEqualRoles(long id, String ids) {
        if (ids == null) {
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_IDS_NULL);
        }
        List<EqualRole> all = equalRolesResposity.findAll();
        for (int i = 0; i < all.size(); i++) {
            String ids1 = all.get(i).getIds();
            if (ids.equals(ids1)) {
                return;
            }
        }

        String[] split = ids.split(",");
        if (split.length <= 1) {
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_IDS_NUMBER_MUST_BIG_THAN2);
        }

        //要求每个role下的employeeIds相同，ratio相同，否则报异常
        List<RelationRoleAndEmployee> allByRoleId = relationRoleAndEmployeeRepository.getAllByRoleId(Long.valueOf(split[0]));
        for (int i = 1; i < split.length; i++) {
            List<RelationRoleAndEmployee> allByRoleId1 = relationRoleAndEmployeeRepository.getAllByRoleId(Long.valueOf(split[i]));
            if (!isEquels(allByRoleId, allByRoleId1)) {
                throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_MUST_HAVE_SAME_EMPLOYEES);
            }
        }

        Optional<EqualRole> byId = equalRolesResposity.findById(id);
        if (!byId.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_ID_NOT_EXIST);
        }
        try {
            EqualRole equalRole = byId.get();
            equalRole.setIds(ids);
            equalRolesResposity.saveAndFlush(equalRole);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_UPDATE_FAILED);
        }

    }

    @Override
    public void delete(long id) {
        Optional<EqualRole> byId = equalRolesResposity.findById(id);
        if(!byId.isPresent()){
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_ID_NOT_EXIST);
        }
        try {
            equalRolesResposity.deleteById(id);
        }catch (Exception e){
            throw new ProgramScheduleException(ResultEnum.EQUEL_ROLE_DELETE_FAILED);
        }
    }
}
