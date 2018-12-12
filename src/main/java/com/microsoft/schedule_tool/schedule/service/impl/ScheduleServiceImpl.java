package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.schedule.ScheduleRole;
import com.microsoft.schedule_tool.schedule.service.ProgramRoleService;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.service.schedule.ScheduleExcelService;
import com.microsoft.schedule_tool.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/12
 **/
@Service
public class ScheduleServiceImpl implements ScheduleSercive {

    @Autowired
    private ProgramRoleService programRoleService;

    @Autowired
    private RelationRoleAndEmployeeService relationRoleAndEmployeeService;

    private ArrayList<ScheduleRole> scheduleRoles = new ArrayList<>();

    //一周中不可选的员工
    private ArrayList<Integer> notOptionalInOneWeek = new ArrayList<>();


    private int weekNums;

    private Long[][] result;

    private List<ProgramRole> programRoles;

    @Override
    public void schedule(String from, String to) {
        // TODO: 2018/12/12  check params
        try {
            initParams(from, to);


        } catch (Exception e) {

        }
    }

    private void initParams(String from, String to) throws ParseException {
        int startWeek = DateUtil.getWeekOfYear(from);
        int endWeek = DateUtil.getWeekOfYear(to);
        weekNums = endWeek - startWeek + 1;

        programRoles = programRoleService.findAll();

        result = new Long[programRoles.size()][weekNums];

        //init scheduleRoles
        for (int i = 0; i < programRoles.size(); i++) {
            ScheduleRole scheduleRole = new ScheduleRole();

            Long id = programRoles.get(i).getId();
            List<StationEmployee> employees = relationRoleAndEmployeeService.getAllWorkersByRoleId(id);
            int maxRatio;
            for (int j = 0; j < employees.size(); j++) {

            }

            scheduleRole.id = id;
            scheduleRole.stationEmployees = (ArrayList<StationEmployee>) employees;
            scheduleRole.currentRatio=1;

        }
    }
}
