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
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainerTest;
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

    @Override
    public ProgramScheduleContainerTest getProgramScheduleTest(String from, String to) {

        try {
            return test(from, to);
        } catch (ParseException e) {
            throw new RuntimeException("date format is not proper...");
        }
    }

    private ProgramScheduleContainerTest test(String from , String to) throws ParseException {

        int fromWeek= DateUtil.getWeekOfYear(from);
        int toWeek=DateUtil.getWeekOfYear(to);
        int weekCount=toWeek-fromWeek+1;

        List<ProgramScheduleTest> target=new ArrayList<>();
        List<Program> programs=mProgramRepository.findAll();


        int index0=0;

        int index1_0=0;
        int index1_1=index1_0+1;

        int index2_0=0;
        int index2_1=index2_0+1;

        String nizaoA="";
        String nizaoB="";

        for(int i=0;i<weekCount;i++){

            Map<String,String> programMap=new HashMap<>();

            for (Program program:programs){
                String programName=program.getName();
                if(programName.equals(Constants.PROGRAMS[0])){//kandongfang
                    List<ProgramEmployee> employees0=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),0);
                    programMap.put(programName,employees0.get(index0%employees0.size()).getName()+","+employees0.get((index0+1)%employees0.size()).getName());
                    index0+=2;
                }else if(programName.equals(Constants.PROGRAMS[1])){//nizaojingjinji
                    List<ProgramEmployee> employees0=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),0);
                    List<ProgramEmployee> employees1=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),1);
                    nizaoA=employees1.get(index1_0%employees1.size()).getName();
                    nizaoB=employees0.get(index0%employees0.size()).getName();
                    programMap.put(programName,nizaoA+","+nizaoB);
                    index0++;
                    index1_0++;
                }else if(programName.equals(Constants.PROGRAMS[6])){
                    List<ProgramEmployee> employees0=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),0);
                    programMap.put(programName,employees0.get(index0%employees0.size()).getName()+","+employees0.get((index0+1)%employees0.size()).getName());
                    index0+=2;
                }else if(programName.equals(Constants.PROGRAMS[2])){//jinrishiwanjia
                    List<ProgramEmployee> employees0=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),0);
                    List<ProgramEmployee> employees1=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),1);
                    List<ProgramEmployee> employees2=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),2);

                    int delta=employees1.size()-employees2.size();
                    if(delta>0&&employees1.size()%employees2.size()!=0){// 类别A~C的人数大于类别a~b人数
                        if((index1_1-1)%employees1.size()>employees2.size()||(index1_1-1)%employees1.size()==0){
                            programMap.put(programName,employees1.get(index1_1%employees1.size()).getName()+","+employees0.get(index0%employees0.size()).getName());
                            index1_1++;
                            index0++;

                            index2_0++;//important,轮空系数也要递增
                        }else{
                            programMap.put(programName,employees1.get(index1_1%employees1.size()).getName()+","+employees2.get(index2_0%employees2.size()).getName());
                            index1_1++;
                            index2_0++;
                        }
                    }else if(delta<0&&employees2.size()%employees1.size()!=0){// 类别A~C的人数小于类别a~b人数
                        if(index2_0%employees2.size()>employees1.size()||index2_0%employees2.size()==0){
                            programMap.put(programName,employees0.get(index0%employees0.size()).getName()+","+employees2.get(index2_0%employees2.size()).getName());
                            index0++;
                            index2_0++;

                            index1_1++;//important,轮空系数也要递增
                        }else{
                            programMap.put(programName,employees1.get(index1_1%employees1.size()).getName()+","+employees2.get(index2_0%employees2.size()).getName());
                            index1_1++;
                            index2_0++;
                        }
                    }else{// 类别A~C的人数等于类别a~b人数
                        programMap.put(programName,employees1.get(index1_1%employees1.size()).getName()+","+employees2.get(index2_0%employees2.size()).getName());
                        index1_1++;
                        index2_0++;
                    }
                }else if(programName.equals(Constants.PROGRAMS[3])){// naokexiu
                    List<ProgramEmployee> employees2=mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(),2);
                    programMap.put(programName,employees2.get(index2_1%employees2.size()).getName());
                    index2_1++;
                }else if(programName.equals(Constants.PROGRAMS[5])){// jiance
                    programMap.put(programName,programMap.get(Constants.PROGRAMS[3]));
                }else if(programName.equals(Constants.PROGRAMS[4])){//tianqibobao
                    programMap.put(programName,nizaoB);
                }
            }

            ProgramScheduleTest programSchedule=new ProgramScheduleTest();
            programSchedule.setProgramMap(programMap);
            target.add(programSchedule);
        }

        ProgramScheduleContainerTest container=new ProgramScheduleContainerTest();
        container.setFrom(from);
        container.setTo(to);
        container.setProgramScheduleList(target);
        return container;
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
