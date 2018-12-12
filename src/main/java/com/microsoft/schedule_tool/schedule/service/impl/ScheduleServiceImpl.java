package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RelationRoleAndEmployee;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.schedule.ScheduleRoleWaitingList;
import com.microsoft.schedule_tool.schedule.repository.ProgramRoleRepository;
import com.microsoft.schedule_tool.schedule.repository.RelationRoleAndEmployeeRepository;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author kb_jay
 * @time 2018/12/12
 **/
@Service
public class ScheduleServiceImpl implements ScheduleSercive {

    @Autowired
    private ProgramRoleRepository programRoleRepository;

    private ArrayList<ScheduleRoleWaitingList> scheduleRoles;

    //一周中不可选的员工
    private ArrayList<Long> notOptionalInOneWeek;


    private int weekNums;

    private Long[][] result;


    @Override
    public void schedule(String from, String to) {
        // TODO: 2018/12/12  check params
        try {
            initParams(from, to);
            for (int i = 0; i < result[0].length; i++) {
                for (int j = 0; j < result.length; j++) {
                    //i->time  j->role
                    ScheduleRoleWaitingList scheduleRole = scheduleRoles.get(j);
                    Queue<Long> alternativeEmployee = scheduleRole.alternativeEmployee;
                    while (!alternativeEmployee.isEmpty() && isInNotOptionalInOneWeek(alternativeEmployee.peek())) {
                        Long temp = alternativeEmployee.poll();
                        alternativeEmployee.offer(temp);
                        // TODO: 2018/12/12
                        //这里如果每一个员工都不可用会死循环！！！
                    }
                    Long employeeId = alternativeEmployee.poll();
                    result[i][j] = employeeId;
                    scheduleRole.alternativeEmployee = alternativeEmployee;
                    addToNotOptionalInOneWeek(employeeId);

                    if (alternativeEmployee.isEmpty()) {
                        //同一个ratio排完了,更新候选名单
                        scheduleRole.updateAlternativeEmloyee();
                    }
                }
                //reset
                notOptionalInOneWeek.clear();
            }

            for (int i = 0; i < result[0].length; i++) {
                for (int j = 0; j < result.length; j++) {
                    System.out.print(result[i][j] + " ");
                }
                System.out.println();
            }

        } catch (Exception e) {

        }
    }


    private boolean isInNotOptionalInOneWeek(long employeeId) {
        for (int i = 0; i < notOptionalInOneWeek.size(); i++) {
            if (notOptionalInOneWeek.get(i) == employeeId) {
                return true;
            }
        }
        return false;
    }

    //添加自己以及跟自己互斥的员工进入周不可选名单
    // TODO: 2018/12/12
    private void addToNotOptionalInOneWeek(long employeeId) {
        notOptionalInOneWeek.add(employeeId);
    }

    private void initParams(String from, String to) throws ParseException {
        int startWeek = DateUtil.getWeekOfYear(from);
        int endWeek = DateUtil.getWeekOfYear(to);
        weekNums = endWeek - startWeek + 1;

        List<ProgramRole> programRoles = programRoleRepository.findAll();

        //init result
        result = new Long[programRoles.size()][weekNums];
        //init notOptionalInOneWeek
        notOptionalInOneWeek = new ArrayList<>();
        //init scheduleRoles
        scheduleRoles = new ArrayList<>();
        for (int i = 0; i < programRoles.size(); i++) {
            ScheduleRoleWaitingList scheduleRole = new ScheduleRoleWaitingList();
            scheduleRole.init(programRoles.get(i));
            scheduleRoles.add(scheduleRole);
        }
    }
}
