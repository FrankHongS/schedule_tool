package com.microsoft.schedule_tool.service.impl.schedule;

import com.microsoft.schedule_tool.dao.schedule.ProgramEmployeeRepository;
import com.microsoft.schedule_tool.dao.schedule.ProgramRepository;
import com.microsoft.schedule_tool.entity.schedule.Program;
import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;
import com.microsoft.schedule_tool.entity.schedule.SpecialEmployee;
import com.microsoft.schedule_tool.service.schedule.ProgramEmployeeService;
import com.microsoft.schedule_tool.service.schedule.ScheduleExcelService;
import com.microsoft.schedule_tool.service.schedule.SpecialEmployeeService;
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

    @Autowired
    private SpecialEmployeeService mSpecialEmployeeService;

    @Override
    public ProgramScheduleContainer getProgramSchedule(String from, String to) {
        try {
            int fromWeek = DateUtil.getWeekOfYear(from);
            int toWeek = DateUtil.getWeekOfYear(to);
            int weekCount = toWeek - fromWeek + 1;

            ProgramScheduleContainer container = new ProgramScheduleContainer();
            container.setFrom(from);
            container.setTo(to);

            List<Program> programs = mProgramRepository.findAll();
            List<ProgramSchedule> target = new ArrayList<>();

            for (Program program : programs) {
                ProgramSchedule programSchedule = new ProgramSchedule();
                programSchedule.setProgramName(program.getName());

                List<ProgramEmployee> employees = mProgramEmployeeService.getAllProgramEmployeesByProgramId(program.getId());
                if (employees.size() >= 2) {
                    List<String> nameList = new ArrayList<>();

                    int employeesSize = employees.size();
                    for (int i = 0; i < weekCount; i++) {
                        String names = employees.get(2 * i % employeesSize).getName() + "," + employees.get((2 * i + 1) % employeesSize).getName();
                        nameList.add(names);
                    }

                    programSchedule.setEmployeeList(nameList);
                } else {
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
            List<SpecialEmployee> specialEmployees = mSpecialEmployeeService.getAllSpecialEmployees();
            List<String> specialNames = new ArrayList<>();
            for (SpecialEmployee employee : specialEmployees) {
                specialNames.add(employee.getName());
            }
            return test(from, to, specialNames);
        } catch (ParseException e) {
            throw new RuntimeException("date format is not proper...");
        }
    }

    private ProgramScheduleContainerTest test(String from, String to, List<String> specialNames) throws ParseException {

        int fromWeek = DateUtil.getWeekOfYear(from);
        int toWeek = DateUtil.getWeekOfYear(to);
        int weekCount = toWeek - fromWeek + 1;

        List<ProgramScheduleTest> target = new ArrayList<>();
        List<Program> programs = mProgramRepository.findAll();


        int index0 = 0;

        int index1_0 = 0;
        int index1_1 = index1_0 + 1;

        int index2_0 = 0;
        int index2_1 = index2_0 + 1;

        String nizaoA = "";
        String nizaoB = "";

        int[] x = new int[]{-1, -1};

        for (int i = 0; i < weekCount; i++) {

            Map<String, String> programMap = new HashMap<>();
            Map<String, Boolean> workInWeekendMap = new HashMap<>();

            for (Program program : programs) {
                String programName = program.getName();
                if (programName.equals(Constants.PROGRAMS[0])) {//kandongfang
                    List<ProgramEmployee> employees0 = mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(), 0);

                    String[] names = generateNames(employees0, specialNames, index0, x);

                    programMap.put(programName, names[0] + "," + names[1]);

                    index0 += 2;


                } else if (programName.equals(Constants.PROGRAMS[1])) {//nizaojingjinji
                    List<ProgramEmployee> employees0 = mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(), 0);
                    List<ProgramEmployee> employees1 = mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(), 1);
                    nizaoA = employees1.get(index1_0 % employees1.size()).getName();

                    if (index0 == x[1]) {
                        nizaoB = employees0.get(x[0] % employees0.size()).getName();
                    } else {
                        nizaoB = employees0.get(index0 % employees0.size()).getName();
                    }

                    if(index1_0/employees1.size()%2==1){//一个周期之后角色交换
                        String temp=nizaoA;
                        nizaoA=nizaoB;
                        nizaoB=temp;
                    }

                    programMap.put(programName, nizaoA + "," + nizaoB);
                    index0++;
                    index1_0++;
                } else if (programName.equals(Constants.PROGRAMS[6])) {// Demo
                    List<ProgramEmployee> employees0 = mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(), 0);

                    String[] names = generateNames(employees0, specialNames, index0, x);

                    programMap.put(programName, names[0] + "," + names[1]);

                    index0 += 2;
                } else if (programName.equals(Constants.PROGRAMS[2])) {//jinrishiwanjia
                    List<ProgramEmployee> employees0 = mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(), 0);
                    List<ProgramEmployee> employees1 = mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(), 1);
                    List<ProgramEmployee> employees2 = mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(), 2);

                    int delta = employees1.size() - employees2.size();
                    String jinriA="";
                    String jinriB="";
                    if (delta > 0 && employees1.size() % employees2.size() != 0) {// 类别A~C的人数大于类别a~b人数
                        if ((index1_1 - 1) % employees1.size() >= employees2.size()) {
                            jinriA=employees1.get(index1_1 % employees1.size()).getName();
                            jinriB=employees0.get(index0 % employees0.size()).getName();
                            index1_1++;
                            index0++;

                            index2_0++;//important,轮空系数也要递增
                        } else {
                            jinriA=employees1.get(index1_1 % employees1.size()).getName();
                            jinriB=employees2.get(index2_0 % employees2.size()).getName();
                            index1_1++;
                            index2_0++;
                        }

                        if((index1_1-2)/employees1.size()%2==1){//一个周期之后角色交换,以人数多的那个为准
                            String temp=jinriA;
                            jinriA=jinriB;
                            jinriB=temp;
                        }

                        programMap.put(programName, jinriA + "," + jinriB);
                    } else if (delta < 0 && employees2.size() % employees1.size() != 0) {// 类别A~C的人数小于类别a~b人数
                        if (index2_0 % employees2.size() >= employees1.size()) {
                            jinriA=employees0.get(index0 % employees0.size()).getName();
                            jinriB=employees2.get(index2_0 % employees2.size()).getName();

                            index0++;
                            index2_0++;

                            index1_1++;//important,轮空系数也要递增
                        } else {
                            jinriA=employees1.get(index1_1 % employees1.size()).getName();
                            jinriB=employees2.get(index2_0 % employees2.size()).getName();

                            index1_1++;
                            index2_0++;
                        }

                        if((index2_0-1)/employees2.size()%2==1){
                            String temp=jinriA;
                            jinriA=jinriB;
                            jinriB=temp;
                        }

                        programMap.put(programName, jinriA + "," + jinriB);
                    } else {// 类别A~C的人数等于类别a~b人数
                        jinriA=employees1.get(index1_1 % employees1.size()).getName();
                        jinriB=employees2.get(index2_0 % employees2.size()).getName();

                        index1_1++;
                        index2_0++;

                        if((index2_0-1)/employees2.size()%2==1){
                            String temp=jinriA;
                            jinriA=jinriB;
                            jinriB=temp;
                        }
                        programMap.put(programName, jinriA + "," + jinriB);
                    }
                } else if (programName.equals(Constants.PROGRAMS[3])) {// naokexiu
                    List<ProgramEmployee> employees2 = mProgramEmployeeService.getAllProgramEmployeesByProgramIdAndEmployeeType(program.getId(), 2);
                    programMap.put(programName, employees2.get(index2_1 % employees2.size()).getName());
                    index2_1++;
                } else if (programName.equals(Constants.PROGRAMS[5])) {// jiance
                    programMap.put(programName, programMap.get(Constants.PROGRAMS[3]));
                } else if (programName.equals(Constants.PROGRAMS[4])) {//tianqibobao
                    programMap.put(programName, nizaoB);
                }

                workInWeekendMap.put(programName, program.getWorkInWeekend());
            }

            ProgramScheduleTest programSchedule = new ProgramScheduleTest();
            programSchedule.setProgramMap(programMap);
            programSchedule.setWorkInWeekendMap(workInWeekendMap);
            target.add(programSchedule);
        }

        ProgramScheduleContainerTest container = new ProgramScheduleContainerTest();
        container.setFrom(from);
        container.setTo(to);
        container.setProgramScheduleList(target);
        return container;
    }

    private int[][] transfer1(int count) {
        int x = 0;
        int y = 0;

        int[][] targetArray = new int[4][2];

        for (int i = 0; i < count; i++) {
            int[] item = new int[2];

            item[0] = i + 1;

            if (i == count - 1) {
                item[1] = 1;
            } else {
                item[1] = i + 2;
            }

            targetArray[i] = item;
        }

        return targetArray;
    }

    /**
     * 如果特殊人员同时值班一个节目的话，让第二个人与下一个人换，以此类推，直到同一个节目不出现两个特殊人员
     *
     * @param employees0
     * @param specialNames
     * @param index0
     * @param x
     * @return
     */
    private String[] generateNames(List<ProgramEmployee> employees0, List<String> specialNames, int index0, int[] x) {
        String first;
        String second;

        if (index0 == x[1]) {
            first = employees0.get(x[0] % employees0.size()).getName();
            second = employees0.get((index0 + 1) % employees0.size()).getName();
        } else if (index0 + 1 == x[1]) {
            first = employees0.get(index0 % employees0.size()).getName();
            second = employees0.get(x[0] % employees0.size()).getName();
        } else {
            first = employees0.get(index0 % employees0.size()).getName();
            second = employees0.get((index0 + 1) % employees0.size()).getName();
            x[0] = index0 + 1;
            x[1] = index0 + 1;
        }

        while (specialNames.contains(first) && specialNames.contains(second)) {
            x[1] = x[1] + 1;
            second = employees0.get(x[1] % employees0.size()).getName();
        }

        return new String[]{first, second};
    }

}
