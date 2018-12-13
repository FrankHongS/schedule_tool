package com.microsoft.schedule_tool.service.impl.schedule;

import com.microsoft.schedule_tool.dao.schedule.ProgramEmployeeRepository;
import com.microsoft.schedule_tool.dao.schedule.SpecialEmployeeRepository;
import com.microsoft.schedule_tool.entity.schedule.SpecialEmployee;
import com.microsoft.schedule_tool.service.schedule.SpecialEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Frank Hon on 11/26/2018
 * E-mail: v-shhong@microsoft.com
 */
@Service
public class SpecialEmployeeServiceImpl implements SpecialEmployeeService {

    @Autowired
    private SpecialEmployeeRepository mSpecialEmployeeRepository;

    @Autowired
    private ProgramEmployeeRepository mProgramEmployeeRepository;

    @Override
    public List<SpecialEmployee> getAllSpecialEmployees() {
        return mSpecialEmployeeRepository.findAll();
    }

    @Override
    @Transactional
    public SpecialEmployee saveSpecialEmployee(SpecialEmployee employee) {

        if(employee==null||employee.getName()==null||"".equals(employee.getName())){
            throw new RuntimeException("name can't be empty");
        }

        if(!mProgramEmployeeRepository.findByName(employee.getName()).isPresent()){//特殊人员需要首先在人员表中保存
            throw new RuntimeException("employee should be saved in Program first");
        }

        try {
            SpecialEmployee specialEmployee=mSpecialEmployeeRepository.save(employee);
            if (specialEmployee!=null){
                return specialEmployee;
            }else{
                throw new RuntimeException("fail to save employee");
            }
        }catch (Exception e){
            throw new RuntimeException("fail to save employee");
        }
    }

    @Override
    @Transactional
    public boolean deleteSpecialEmployee(Integer id) {

        if(!mSpecialEmployeeRepository.findById(id).isPresent()){
            throw new RuntimeException("employee id not existing");
        }

        mSpecialEmployeeRepository.deleteById(id);

        if(mSpecialEmployeeRepository.findById(id).isPresent()){
            throw new RuntimeException("fail to delete employee");
        }

        return true;
    }
}
