package com.microsoft.schedule_tool.schedule.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.schedule_tool.exception.schedule.ProgramEmployeeException;
import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.request.ReqRole;
import com.microsoft.schedule_tool.schedule.repository.StationEmployeeRepository;
import com.microsoft.schedule_tool.schedule.service.StationEmployeeService;
import com.microsoft.schedule_tool.util.StringUtils;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
@Service
public class StationEmployeeServiceImpl implements StationEmployeeService {
    @Autowired
    private StationEmployeeRepository stationEmployeeRepository;

    @Override
    public long save(String name, String alias) {
        //check param(not null, alias not repeat)
        if (StringUtils.isEmpty(alias)) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ALIAS_NULL);
        }
        if (StringUtils.isEmpty(name)) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_NAME_NULL);
        }
        Optional<StationEmployee> byAlias = stationEmployeeRepository.findByAlias(alias);
        if (byAlias.isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ALIAS_REPEAT);
        }
        try {
            StationEmployee stationEmployee = new StationEmployee();
            stationEmployee.setAlias(alias);
            stationEmployee.setDeleted(false);
            stationEmployee.setName(name);
            stationEmployee.setCreateDate(new Date(new java.util.Date().getTime()));
            StationEmployee save = stationEmployeeRepository.save(stationEmployee);
            return save.getId();
        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_SAVE_FAIL);
        }
    }

    @Override
    public void remove(long id) {
        Optional<StationEmployee> byId = stationEmployeeRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ID_NOT_EXIST);
        }
        //get
        //change
        //save
        StationEmployee stationEmployee = byId.get();
        stationEmployee.setDeleted(true);
        try {
            stationEmployeeRepository.saveAndFlush(stationEmployee);
        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_DELETE_FAIL);
        }
    }

    @Override
    public void update(long id, String name, String alias) {
        if (StringUtils.isEmpty(alias)) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ALIAS_NULL);
        }
        if (StringUtils.isEmpty(name)) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_NAME_NULL);
        }

        Optional<StationEmployee> byId = stationEmployeeRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ID_NOT_EXIST);
        }

        StationEmployee originalEmployee=byId.get();

        if (!alias.equals(originalEmployee.getAlias())&&stationEmployeeRepository.findByAlias(alias).isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_ALIAS_REPEAT);
        }

        //get
        //change
        //save
        try {
            StationEmployee stationEmployee = byId.get();
            stationEmployee.setName(name);
            stationEmployee.setAlias(alias);
            stationEmployeeRepository.saveAndFlush(stationEmployee);
        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_UPDATE_FAIL);
        }
    }

    @Override
    public long[] saveSome(String jsonData) {
        List<ReqEmployee> employees = new ArrayList<>();
        try {
            ObjectMapper om = new ObjectMapper();
            JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, ReqEmployee.class);
            employees = om.readValue(jsonData, javaType);
            if (employees.size() <= 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ProgramException(ResultEnum.PROGRAM_EMPLOYEE_WRONG_SAVE_PARAMS);
        }
        long[] re = new long[employees.size()];
        for (int i = 0; i < re.length; i++) {
            re[i] = save(employees.get(i).name, employees.get(i).alias);
        }
        return re;
    }

    @Override
    public List<StationEmployee> findAll() {
        //isDeleted =false
        try{
            List<StationEmployee> allByIsDeleted = stationEmployeeRepository.findAllByIsDeleted(false);
            return allByIsDeleted;
        }catch (Exception e){
            throw new ProgramEmployeeException(ResultEnum.EMPLOYEE_FIND_FAILED);
        }
    }
}
