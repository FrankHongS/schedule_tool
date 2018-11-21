package com.microsoft.schedule_tool.service.impl.schedule;

import com.microsoft.schedule_tool.dao.schedule.ProgramEmployeeRepository;
import com.microsoft.schedule_tool.dao.schedule.ProgramRepository;
import com.microsoft.schedule_tool.entity.schedule.Program;
import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;
import com.microsoft.schedule_tool.service.schedule.ScheduleExcelService;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.schedule.ProgramSchedule;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
@Service
public class ScheduleExcelServiceImpl implements ScheduleExcelService {

    @Autowired
    private ProgramRepository mProgramRepository;

    @Autowired
    private ProgramEmployeeRepository mProgramEmployeeRepository;

    @Override
    public ProgramScheduleContainer getProgramSchedule(String from, String to) {
        try {
            int fromWeek= DateUtil.getWeekOfYear(from);
            int toWeek=DateUtil.getWeekOfYear(to);
            int weekCount=toWeek-fromWeek+1;

            ProgramScheduleContainer container=new ProgramScheduleContainer();
            container.setFrom(from);
            container.setTo(to);

            List<Program> programs=mProgramRepository.findAll();
            List<ProgramSchedule> target=new ArrayList<>();

            for(Program program:programs){
                ProgramSchedule programSchedule=new ProgramSchedule();
                programSchedule.setProgramName(program.getName());

                List<ProgramEmployee> employees=mProgramEmployeeRepository.findByProgramId(program.getId());
                if(employees.size()>=2){
                    List<String> nameList=new ArrayList<>();

                    int employeesSize=employees.size();
                    for(int i=0;i<weekCount;i++){
                        String names=employees.get(2*i%employeesSize).getName()+","+employees.get((2*i+1)%employeesSize).getName();
                        nameList.add(names);
                    }

                    programSchedule.setEmployeeList(nameList);
                }else{
                    throw new RuntimeException("there is a program whose employees is less than 2");
                }

                target.add(programSchedule);
            }

            container.setProgramSchedules(target);

            return container;

        } catch (ParseException e) {
            throw new RuntimeException("date format is not proper...");
        }
    }
}
