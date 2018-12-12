package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.exception.schedule.ProgramEmployeeException;
import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RelationRoleAndEmployee;
import com.microsoft.schedule_tool.schedule.domain.entity.RoleEmployeeMultiKeysClass;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.repository.ProgramRoleRepository;
import com.microsoft.schedule_tool.schedule.repository.RelationRoleAndEmployeeRepository;
import com.microsoft.schedule_tool.schedule.repository.StationEmployeeRepository;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
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
public class RelationRoleAndEmployeeServiceImpl implements RelationRoleAndEmployeeService {
    @Autowired
    public RelationRoleAndEmployeeRepository repository;

    @Autowired
    public ProgramRoleRepository programRoleRepository;

    @Autowired
    public StationEmployeeRepository stationEmployeeRepository;

    @Override
    public List<StationEmployee> getAllWorkersByRoleId(long id) {
        //check params
        Optional<ProgramRole> byId = programRoleRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_ROLE_ID_NOT_EXIST);
        }
        try {
            List<StationEmployee> employees = new ArrayList<>();

            List<RelationRoleAndEmployee> allByRole = repository.getAllByRoleId(id);

            for (int i = 0; i < allByRole.size(); i++) {
                Long employeeId = allByRole.get(i).getEmployeeId();
                Optional<StationEmployee> byId1 = stationEmployeeRepository.findById(employeeId);
                if(byId1.isPresent()){
                    StationEmployee stationEmployee = byId1.get();
                    if(!stationEmployee.isDeleted()){
                        employees.add(stationEmployee);
                    }
                }
            }
            return employees;
        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_ROLE_AND_EMPLOYEE_FIND_ERROR);
        }
    }

    @Override
    public void addWorkers2Role(long employeeId, long roleId, double ratio) {
        //check params
        Optional<StationEmployee> employeeRepositoryById = stationEmployeeRepository.findById(employeeId);
        if (!employeeRepositoryById.isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ID_NOT_EXIST);
        }
        StationEmployee stationEmployee = employeeRepositoryById.get();

        Optional<ProgramRole> byId = programRoleRepository.findById(roleId);
        if (!byId.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_ID_NOT_EXIST);
        }
        ProgramRole programRole = byId.get();

        if (ratio < 0 || ratio > 1) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_ROLE_AND_EMPLOYEE_WRONG_RATIO);
        }

        try {
            RelationRoleAndEmployee relationRoleAndEmployee = new RelationRoleAndEmployee();
            relationRoleAndEmployee.setEmployeeId(employeeId);
            relationRoleAndEmployee.setRoleId(roleId);
            relationRoleAndEmployee.setRatio(ratio);

            repository.save(relationRoleAndEmployee);
        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_ROLE_SAVE_EMPLOYEE_ERROR);
        }
    }

    @Override
    public void removeWorkersByRole(long employeeId, long roleId) {
        //checkParams
        Optional<StationEmployee> employeeRepositoryById = stationEmployeeRepository.findById(employeeId);
        if (!employeeRepositoryById.isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ID_NOT_EXIST);
        }

        Optional<ProgramRole> byId = programRoleRepository.findById(roleId);
        if (!byId.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_ID_NOT_EXIST);
        }

        try {
            RoleEmployeeMultiKeysClass id = new RoleEmployeeMultiKeysClass();
            id.setEmployeeId(employeeId);
            id.setRoleId(roleId);
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_ROLE_REMOVE_EMPLOYEE_ERROR);
        }
    }

    @Override
    public void changeRatio(long employeeId, long roleId, double ratio) {
        //check params
        Optional<StationEmployee> employeeRepositoryById = stationEmployeeRepository.findById(employeeId);
        if (!employeeRepositoryById.isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ID_NOT_EXIST);
        }

        Optional<ProgramRole> byId = programRoleRepository.findById(roleId);
        if (!byId.isPresent()) {
            throw new ProgramException(ResultEnum.PROGRAM_ROLE_ID_NOT_EXIST);
        }

        if (ratio < 0 || ratio > 1) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_ROLE_AND_EMPLOYEE_WRONG_RATIO);
        }
        try {
            RoleEmployeeMultiKeysClass id = new RoleEmployeeMultiKeysClass();
            id.setRoleId(roleId);
            id.setEmployeeId(employeeId);

            Optional<RelationRoleAndEmployee> repositoryById = repository.findById(id);
            if (repositoryById.isPresent()) {
                RelationRoleAndEmployee relationRoleAndEmployee = repositoryById.get();
                relationRoleAndEmployee.setRatio(ratio);
                repository.saveAndFlush(relationRoleAndEmployee);
            }
        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_ROLE_AND_EMPLOYEE_CHANGE_RATIO_ERROR);
        }

    }
}
