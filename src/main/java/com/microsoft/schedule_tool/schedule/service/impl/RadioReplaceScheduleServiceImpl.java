package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.dao.schedule.ProgramRepository;
import com.microsoft.schedule_tool.entity.Employee;
import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioReplaceSchedule;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioSchedule;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespReplaceSchedule;
import com.microsoft.schedule_tool.schedule.repository.*;
import com.microsoft.schedule_tool.schedule.service.RadioReplaceScheduleService;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/18
 **/
@Service
public class RadioReplaceScheduleServiceImpl implements RadioReplaceScheduleService {
    @Autowired
    private RadioReplaceScheduleReposity radioReplaceScheduleReposity;

    @Autowired
    private ProgramRoleRepository programRoleRepository;

    @Autowired
    private StationEmployeeRepository stationEmployeeRepository;

    @Autowired
    private RadioProgramRepository radioProgramRepository;

    @Autowired
    private RadioScheduleRepository radioScheduleRepository;


    @Override
    public long addReplace(long roleId, String date, long employeeId) {
        //checkParams
        Optional<ProgramRole> programRoleOptional = programRoleRepository.findById(roleId);
        if (!programRoleOptional.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.PROGRAM_ROLE_ID_NOT_EXIST);
        }

        Optional<StationEmployee> stationEmployeeOptional = stationEmployeeRepository.findById(employeeId);
        if (!stationEmployeeOptional.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.EMPLOYEE_ID_NOT_EXIST);
        }

        Date d = null;
        try {
            d = DateUtil.parseDateString(date);
        } catch (ParseException e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_REPLACE_DATE_PARSE_ERROR);
        }

        Optional<RadioSchedule> radioScheduleOptional = radioScheduleRepository.findByDateAndRole(d, programRoleOptional.get());
        if (!radioScheduleOptional.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_DATE_AND_ROLE_NOT_EXTST);
        }

        List<RadioReplaceSchedule> all = radioReplaceScheduleReposity.findAll();

        for (int i = 0; i < all.size(); i++) {
            RadioReplaceSchedule radioReplaceSchedule = all.get(i);
            if (radioReplaceSchedule.getEmployee().getId().equals(employeeId)
                    && radioReplaceSchedule.getRadioSchedule().getDate().getTime() == d.getTime()
                    && radioReplaceSchedule.getRadioSchedule().getRole().getId().equals(roleId)) {
                throw new ProgramScheduleException(ResultEnum.SCHEDULE_REPLACE_REPEAT);
            }
        }

        try {
            RadioReplaceSchedule radioReplaceSchedule = new RadioReplaceSchedule();
            radioReplaceSchedule.setEmployee(stationEmployeeOptional.get());
            radioReplaceSchedule.setRadioSchedule(radioScheduleOptional.get());
            RadioReplaceSchedule save = radioReplaceScheduleReposity.save(radioReplaceSchedule);
            return save.getId();
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_REPLACE_FAILED);
        }
    }

    @Override
    public void deleteReplace(long replaceId) {
        Optional<RadioReplaceSchedule> radioReplaceScheduleOptional = radioReplaceScheduleReposity.findById(replaceId);
        if (!radioReplaceScheduleOptional.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_REPLACE_ID_NOT_EXIST);
        }
        try {
            radioReplaceScheduleReposity.deleteById(replaceId);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_REPLACE_DELETE_FAILED);
        }
    }

    @Override
    public List<RespReplaceSchedule> getAllReplace(String from, String to) {

        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = DateUtil.parseDateString(from);
            toDate = DateUtil.parseDateString(to);
        } catch (ParseException e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_REPLACE_DATE_PARSE_ERROR);
        }

        try {
            //getAll  check Date
            List<RadioReplaceSchedule> all = radioReplaceScheduleReposity.findAll();

            List<RespReplaceSchedule> re = new ArrayList<>();
            for (int i = 0; i < all.size(); i++) {
                RadioReplaceSchedule radioReplaceSchedule = all.get(i);
                long id = radioReplaceSchedule.getId();
                StationEmployee employee = radioReplaceSchedule.getEmployee();
                RadioSchedule radioSchedule = radioReplaceSchedule.getRadioSchedule();

                Date date = radioSchedule.getDate();
                if (date.getTime() >= fromDate.getTime()
                        && date.getTime() <= toDate.getTime()) {
                    RespReplaceSchedule respReplaceSchedule = new RespReplaceSchedule();
                    respReplaceSchedule.id = id;
                    respReplaceSchedule.alias = employee.getAlias();
                    respReplaceSchedule.name = employee.getName();
                    respReplaceSchedule.date = DateUtil.parseDateToString(radioSchedule.getDate());
                    respReplaceSchedule.roleName = radioSchedule.getRole().getName();
                    respReplaceSchedule.programName = radioSchedule.getRole().getRadioProgram().getName();
                    respReplaceSchedule.replacedEmployeeAlias = radioSchedule.getEmployee().getAlias();
                    respReplaceSchedule.replacedEmployeeName = radioSchedule.getEmployee().getName();
                    re.add(respReplaceSchedule);
                }
            }
            return re;
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_REPLACE_FIND_FAILED);
        }
    }

    @Override
    public List<RespReplaceSchedule> getAllReplace() {
        try {
            //getAll  check Date
            List<RadioReplaceSchedule> all = radioReplaceScheduleReposity.findAll();

            List<RespReplaceSchedule> re = new ArrayList<>();
            for (int i = 0; i < all.size(); i++) {
                RadioReplaceSchedule radioReplaceSchedule = all.get(i);
                long id = radioReplaceSchedule.getId();
                StationEmployee employee = radioReplaceSchedule.getEmployee();
                RadioSchedule radioSchedule = radioReplaceSchedule.getRadioSchedule();
                RespReplaceSchedule respReplaceSchedule = new RespReplaceSchedule();
                respReplaceSchedule.id = id;
                respReplaceSchedule.alias = employee.getAlias();
                respReplaceSchedule.name = employee.getName();
                respReplaceSchedule.date = DateUtil.parseDateToString(radioSchedule.getDate());
                respReplaceSchedule.roleName = radioSchedule.getRole().getName();
                respReplaceSchedule.programName = radioSchedule.getRole().getRadioProgram().getName();
                respReplaceSchedule.replacedEmployeeAlias = radioSchedule.getEmployee().getAlias();
                respReplaceSchedule.replacedEmployeeName = radioSchedule.getEmployee().getName();
                re.add(respReplaceSchedule);
            }
            return re;
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_REPLACE_FIND_FAILED);
        }

    }
}
