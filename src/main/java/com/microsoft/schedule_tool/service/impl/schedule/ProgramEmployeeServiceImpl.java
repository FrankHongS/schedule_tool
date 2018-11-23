package com.microsoft.schedule_tool.service.impl.schedule;

import com.microsoft.schedule_tool.dao.schedule.ProgramAndEmployeeRepository;
import com.microsoft.schedule_tool.dao.schedule.ProgramEmployeeRepository;
import com.microsoft.schedule_tool.entity.schedule.ProgramAndEmployee;
import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;
import com.microsoft.schedule_tool.exception.schedule.ProgramEmployeeException;
import com.microsoft.schedule_tool.service.schedule.ProgramEmployeeService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
@Service
public class ProgramEmployeeServiceImpl implements ProgramEmployeeService {

    @Autowired
    private ProgramEmployeeRepository mProgramEmployeeRepository;

    @Autowired
    private ProgramAndEmployeeRepository mProgramAndEmployeeRepository;

    @Override
    public List<ProgramEmployee> getAllProgramEmployeesByProgramId(Integer programId) {
        List<ProgramAndEmployee> programAndEmployeeList = mProgramAndEmployeeRepository.findByProgramId(programId);

        List<ProgramEmployee> programEmployeeList = new ArrayList<>();

        for (ProgramAndEmployee programAndEmployee : programAndEmployeeList) {
            Optional<ProgramEmployee> programEmployeeOptional = mProgramEmployeeRepository.findById(programAndEmployee.getEmployeeId());
            if (programEmployeeOptional.isPresent()) {
                ProgramEmployee employee = programEmployeeOptional.get();
                programEmployeeList.add(employee);
            }
        }

        return programEmployeeList;
    }

    @Override
    public List<ProgramEmployee> getAllProgramEmployeesByProgramIdAndEmployeeType(Integer programId, Integer employeeType) {

        List<ProgramEmployee> programEmployeeList=new ArrayList<>();

        List<ProgramEmployee> programEmployees=mProgramEmployeeRepository.findByEmployeeType(employeeType);

        for(ProgramEmployee programEmployee:programEmployees){
            boolean present=mProgramAndEmployeeRepository.findByProgramIdAndEmployeeId(programId,programEmployee.getId()).isPresent();
            if(present){
                programEmployeeList.add(programEmployee);
            }
        }

        return programEmployeeList;
    }

    @Transactional
    @Override
    public ProgramEmployee saveProgramEmployee(ProgramEmployee programEmployee, Integer programId) {

        if (programEmployee == null || programEmployee.getName() == null || "".equals(programEmployee.getName())) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_NAME_NULL);
        }

        Optional<ProgramEmployee> programEmployeeOptional = mProgramEmployeeRepository
                .findByName(programEmployee.getName());
        try {
            if (programEmployeeOptional.isPresent()) {
                ProgramEmployee employee = programEmployeeOptional.get();
                Integer employeeId = employee.getId();
                Optional<ProgramAndEmployee> programAndEmployeeOptional = mProgramAndEmployeeRepository
                        .findByProgramIdAndEmployeeId(programId, employeeId);
                if (!programAndEmployeeOptional.isPresent()) {
                    ProgramAndEmployee programAndEmployee = new ProgramAndEmployee();
                    programAndEmployee.setProgramId(programId);
                    programAndEmployee.setEmployeeId(employeeId);
                    mProgramAndEmployeeRepository.save(programAndEmployee);
                } else {
                    throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_EXIST);
                }

                return employee;
            } else {

                ProgramEmployee result = mProgramEmployeeRepository.save(programEmployee);
                if (result != null) {
                    Integer employeeId = result.getId();
                    ProgramAndEmployee programAndEmployee = new ProgramAndEmployee();
                    programAndEmployee.setProgramId(programId);
                    programAndEmployee.setEmployeeId(employeeId);
                    mProgramAndEmployeeRepository.save(programAndEmployee);
                    return result;
                } else {
                    throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_SAVE_FAIL);
                }
            }

        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_SAVE_FAIL);
        }
    }

    @Transactional
    @Override
    public ProgramEmployee updateProgramEmployee(Integer id, String name, Integer employeeType) {

        if (!mProgramEmployeeRepository.findById(id).isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_ID_NOT_EXIST);
        }

        ProgramEmployee programEmployee = mProgramEmployeeRepository.findById(id).get();

        if (!programEmployee.getName().equals(name)&&mProgramEmployeeRepository.findByName(name).isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_EXIST);
        }

        if(programEmployee.getName().equals(name)){
            programEmployee.setEmployeeType(employeeType);
        }else{
            programEmployee.setName(name);
            programEmployee.setEmployeeType(employeeType);
        }

        try {
            ProgramEmployee result = mProgramEmployeeRepository.save(programEmployee);
            if (result != null) {
                return result;
            } else {
                throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_UPDATE_FAIL);
            }
        } catch (Exception e) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_UPDATE_FAIL);
        }
    }

    @Transactional
    @Override
    public boolean deleteProgramEmployee(Integer id, Integer programId) {

        if (!mProgramEmployeeRepository.findById(id).isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_ID_NOT_EXIST);
        }

        mProgramAndEmployeeRepository.deleteByProgramIdAndEmployeeId(programId, id);

        if (mProgramAndEmployeeRepository.findByProgramIdAndEmployeeId(programId, id).isPresent()) {
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_DELETE_FAIL);
        }

        if (mProgramAndEmployeeRepository.findByEmployeeId(id).size() == 0) {
            mProgramEmployeeRepository.deleteById(id);

            if (mProgramEmployeeRepository.findById(id).isPresent()) {
                throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_DELETE_FAIL);
            }

        }

        return true;
    }
}
