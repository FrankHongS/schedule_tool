package com.microsoft.schedule_tool.service.impl.schedule;

import com.microsoft.schedule_tool.dao.schedule.ProgramEmployeeRepository;
import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;
import com.microsoft.schedule_tool.exception.schedule.ProgramEmployeeException;
import com.microsoft.schedule_tool.service.schedule.ProgramEmployeeService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<ProgramEmployee> getAllProgramEmployeesByProgramId(Integer programId) {
        return mProgramEmployeeRepository.findByProgramId(programId);
    }

    @Transactional
    @Override
    public ProgramEmployee saveProgramEmployee(ProgramEmployee programEmployee) {

        if(programEmployee==null||programEmployee.getName()==null||"".equals(programEmployee.getName())){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_NAME_NULL);
        }

        if(programEmployee.getProgramId()==null){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_PROGRAM_ID_NULL);
        }

        Optional<ProgramEmployee> programEmployeeOptional=mProgramEmployeeRepository
                .findByNameAndProgramId(programEmployee.getName(),programEmployee.getProgramId());

        if(programEmployeeOptional.isPresent()){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_EXIST);
        }
        try{
            ProgramEmployee result=mProgramEmployeeRepository.save(programEmployee);
            if(result!=null){
                return result;
            }else{
                throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_SAVE_FAIL);
            }
        }catch (Exception e){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_SAVE_FAIL);
        }
    }

    @Transactional
    @Override
    public ProgramEmployee updateProgramEmployee(Integer id, String name) {

        if(!mProgramEmployeeRepository.findById(id).isPresent()){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_ID_NOT_EXIST);
        }

        ProgramEmployee programEmployee=mProgramEmployeeRepository.findById(id).get();

        if(mProgramEmployeeRepository.findByNameAndProgramId(name,programEmployee.getProgramId()).isPresent()){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_EXIST);
        }

        programEmployee.setName(name);

        try {
            ProgramEmployee result=mProgramEmployeeRepository.save(programEmployee);
            if(result!=null){
                return result;
            }else{
                throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_UPDATE_FAIL);
            }
        }catch (Exception e){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_UPDATE_FAIL);
        }
    }

    @Override
    public boolean deleteProgramEmployee(Integer id) {

        if(!mProgramEmployeeRepository.findById(id).isPresent()){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_ID_NOT_EXIST);
        }

        mProgramEmployeeRepository.deleteById(id);

        if(mProgramEmployeeRepository.findById(id).isPresent()){
            throw new ProgramEmployeeException(ResultEnum.PROGRAM_EMPLOYEE_DELETE_FAIL);
        }else{
            return true;
        }
    }
}
