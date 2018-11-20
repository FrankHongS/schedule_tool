package com.microsoft.schedule_tool.service.impl.schedule;

import com.microsoft.schedule_tool.dao.schedule.ProgramRepository;
import com.microsoft.schedule_tool.entity.schedule.Program;
import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.service.schedule.ProgramService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
@Service
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    private ProgramRepository mProgramRepository;

    @Override
    public List<Program> getAllPrograms() {
        return mProgramRepository.findAll();
    }

    @Transactional
    @Override
    public Program saveProgram(Program program) {

        if(program==null||program.getName()==null||"".equals(program.getName())){
            throw new ProgramException(ResultEnum.PROGRAM_NAME_NULL);
        }

        if(mProgramRepository.findByName(program.getName()).isPresent()){
            throw new ProgramException(ResultEnum.PROGRAM_NAME_EXIST);
        }

        try{
            Program result=mProgramRepository.save(program);
            if(result==null){
                throw new ProgramException(ResultEnum.PROGRAM_SAVE_FAIL);
            }else{
                return result;
            }
        }catch (Exception e){
            throw new ProgramException(ResultEnum.PROGRAM_SAVE_FAIL);
        }
    }

    @Transactional
    @Override
    public Program updateProgram(Integer id, String name) {

        if(!mProgramRepository.findById(id).isPresent()){
            throw new ProgramException(ResultEnum.PROGRAM_ID_NOT_EXIST);
        }

        if(mProgramRepository.findByName(name).isPresent()){
            throw new ProgramException(ResultEnum.PROGRAM_NAME_EXIST);
        }

        Program program=mProgramRepository.findById(id).get();
        program.setName(name);
        try{
            Program result=mProgramRepository.save(program);
            if(result==null){
                throw new ProgramException(ResultEnum.PROGRAM_UPDATE_FAIL);
            }else{
                return result;
            }
        }catch (Exception e){
            throw new ProgramException(ResultEnum.PROGRAM_UPDATE_FAIL);
        }
    }

    @Transactional
    @Override
    public boolean deleteProgram(Integer id) {

        if(!mProgramRepository.findById(id).isPresent()){
            throw new ProgramException(ResultEnum.PROGRAM_ID_NOT_EXIST);
        }

        mProgramRepository.deleteById(id);

        if(!mProgramRepository.findById(id).isPresent()){
            return true;
        }else{
            throw new ProgramException(ResultEnum.PROGRAM_DELETE_FAIL);
        }
    }
}
