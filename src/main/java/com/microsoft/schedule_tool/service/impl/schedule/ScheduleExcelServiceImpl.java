package com.microsoft.schedule_tool.service.impl.schedule;

import com.microsoft.schedule_tool.dao.schedule.ProgramEmployeeRepository;
import com.microsoft.schedule_tool.dao.schedule.ProgramRepository;
import com.microsoft.schedule_tool.entity.schedule.Program;
import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;
import com.microsoft.schedule_tool.service.schedule.ProgramEmployeeService;
import com.microsoft.schedule_tool.service.schedule.ScheduleExcelService;
import com.microsoft.schedule_tool.util.Constants;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.schedule.ProgramSchedule;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainer;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
@Service
public class ScheduleExcelServiceImpl implements ScheduleExcelService {

    @Autowired
    private ProgramRepository mProgramRepository;

    @Autowired
    private ProgramEmployeeService mProgramEmployeeService;

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

                List<ProgramEmployee> employees=mProgramEmployeeService.getAllProgramEmployeesByProgramId(program.getId());
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

    private void test(){
        List<ProgramScheduleTest> target=new ArrayList<>();
        List<Program> programs=mProgramRepository.findAll();

        ProgramScheduleTest programSchedule=new ProgramScheduleTest();
        List<Map<String,String>> programMapList=new ArrayList<>();
        int index0=0;
        int index1=0;
        int index2=0;
        for (Program program:programs){
            Map<String,String> programMap=new HashMap<>();
            String programName=program.getName();
            if(programName.equals(Constants.PROGRAMS[0])){
                List<ProgramEmployee> employees0=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),0);
                programMap.put(programName,employees0.get(index0%employees0.size())+","+employees0.get((index0+1)%employees0.size()));
                index0+=2;
                programMapList.add(programMap);
            }else if(programName.equals(Constants.PROGRAMS[1])){
                List<ProgramEmployee> employees0=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),0);
                List<ProgramEmployee> employees1=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),1);
                programMap.put(programName,employees1.get(index1%employees1.size())+","+employees0.get(index0%employees0.size()));
                index0++;
                index1++;
                programMapList.add(programMap);
            }else if(programName.equals(Constants.PROGRAMS[6])){
                List<ProgramEmployee> employees0=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),0);
                programMap.put(programName,employees0.get(index0%employees0.size())+","+employees0.get((index0+1)%employees0.size()));
                index0+=2;
                programMapList.add(programMap);
            }else if(programName.equals(Constants.PROGRAMS[2])){
                List<ProgramEmployee> employees0=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),0);
                List<ProgramEmployee> employees1=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),1);
                List<ProgramEmployee> employees2=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),2);

                int delta=employees1.size()-employees2.size();
                if(delta>0){//todo
                    if(index2%employees2.size()==0){
                        programMap.put(programName,employees1.get(index1%employees1.size())+","+employees2.get(index2%employees2.size()));
                        index1++;
                        index2++;
                    }else{
                        programMap.put(programName,employees1.get(index1%employees1.size())+","+employees0.get(index0%employees0.size()));
                        index1++;
                        index0++;
                    }
                    programMapList.add(programMap);
                }
            }
        }
    }

    private int[][] transfer1(int count){
        int x=0;
        int y=0;

        int[][] targetArray=new int[4][2];

        for(int i=0;i<count;i++){
            int[] item=new int[2];

            item[0]=i+1;

            if(i==count-1){
                item[1]=1;
            }else{
                item[1]=i+2;
            }

            targetArray[i]=item;
        }

        return targetArray;
    }
}
