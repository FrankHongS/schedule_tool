package com.microsoft.schedule_tool.schedule.domain.vo.schedule;

import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author kb_jay
 * @time 2018/12/12
 **/
public class ScheduleRole {
    public Long id;
    //最大权重
    public int maxRatio;
    //当前选到那个权重的员工了
    public int currentRatio;
    //备选员工
    public Queue<Integer> alternativeEmployee = new LinkedList<>();
    //所有员工
    public ArrayList<StationEmployee> stationEmployees = new ArrayList<>();
}
