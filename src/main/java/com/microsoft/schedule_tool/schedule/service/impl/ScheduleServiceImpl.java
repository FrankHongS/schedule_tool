package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.exception.schedule.ScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioSchedule;
import com.microsoft.schedule_tool.schedule.domain.entity.RelationRoleAndEmployee;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.schedule.ScheduleRoleWaitingList;
import com.microsoft.schedule_tool.schedule.repository.ProgramRoleRepository;
import com.microsoft.schedule_tool.schedule.repository.RadioScheduleRepository;
import com.microsoft.schedule_tool.schedule.repository.RelationRoleAndEmployeeRepository;
import com.microsoft.schedule_tool.schedule.repository.StationEmployeeRepository;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.omg.CORBA.DATA_CONVERSION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * @author kb_jay
 * @time 2018/12/12
 **/
@Service
public class ScheduleServiceImpl implements ScheduleSercive {

    @Autowired
    private RelationRoleAndEmployeeService relationRoleAndEmployeeService;
    @Autowired
    private RelationRoleAndEmployeeRepository relationRoleAndEmployeeRepository;

    @Autowired
    private ProgramRoleRepository programRoleRepository;

    @Autowired
    private StationEmployeeRepository stationEmployeeRepository;

    @Autowired
    private RadioScheduleRepository radioScheduleRepository;

    private ArrayList<ScheduleRoleWaitingList> scheduleRoles;

    //一周中不可选的员工
    private ArrayList<Long> notOptionalInOneWeek;


    private int weekNums;

    private Long[][] result;
    private List<ProgramRole> hasEmployeeProgramRoles;
    private Date startDate;
    private Date endaDate;
    private int startWeek;
    private int endWeek;


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

                    int moveCount = 0;
                    while (!alternativeEmployee.isEmpty()
                            && moveCount < alternativeEmployee.size()
                            && (isInNotOptionalInOneWeek(alternativeEmployee.peek()) || (i - 1 > 0 && alternativeEmployee.peek().equals(result[j][i - 1])))) {
                        Long temp = alternativeEmployee.poll();
                        alternativeEmployee.offer(temp);
                        moveCount++;
                    }

                    if (moveCount == alternativeEmployee.size()) {
                        //没有可选的人的时候从该角色前面已经排好的人中选一个跟他互换。
                        int move = 0;
                        boolean isChanged = false;
                        while (!alternativeEmployee.isEmpty()
                                && move < alternativeEmployee.size()
                                && !isChanged) {
                            Long employeeId = alternativeEmployee.peek();
                            for (int k = 0; k < i; k++) {
                                //1：选中的人不能在周不可选名单
                                Long temp = result[j][k];
                                if (notOptionalInOneWeek.contains(temp)) {
                                    continue;
                                }
                                //2：被替换的元素左右不能是employeeId
                                if (k - 1 >= 0 && result[j][k - 1].equals(employeeId)) {
                                    continue;
                                }
                                if (k + 1 < i && result[j][k + 1].equals(employeeId)) {
                                    continue;
                                }

                                //3：替换的那一列中不能有employeeId,并且不能有跟employeeId互斥的。
                                boolean canChange = true;
                                for (int l = 0; l < result.length; l++) {
                                    if (employeeId.equals(result[l][k])
                                            || getMutexEmployee(employeeId).contains(result[l][k])) {
                                        canChange = false;
                                        break;
                                    }
                                }
                                if (!canChange) {
                                    continue;
                                }
                                isChanged = true;
                                result[j][k] = alternativeEmployee.poll();
                                result[j][i] = temp;
                                break;
                            }
                            if (!isChanged) {
                                Long temp = alternativeEmployee.poll();
                                alternativeEmployee.offer(temp);
                            }
                            move++;
                        }
                        if (move == alternativeEmployee.size()) {
                            throw new ProgramScheduleException(ResultEnum.SCHEDULE_ERROT_PLEASE_RETRY);
                        }

                    } else {
                        Long employeeId = alternativeEmployee.poll();
                        result[j][i] = employeeId;
                        scheduleRole.alternativeEmployee = alternativeEmployee;
                        addToNotOptionalInOneWeek(employeeId);
                    }
                    if (alternativeEmployee.isEmpty()) {
                        //同一个ratio排完了,更新候选名单
                        scheduleRole.updateAlternativeEmloyee();
                    }
                }
                //reset
                notOptionalInOneWeek.clear();
            }
            //将排好的信息存入db
            saveData2Db();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveData2Db() {
        Date start = DateUtil.getFirstDayOfWeek(DateUtil.getYear(startDate), startWeek-1);

        for (int i = 0; i < result.length; i++) {
            Long roleId = scheduleRoles.get(i).id;
            String cycle = "1111100";
            Optional<ProgramRole> byId = programRoleRepository.findById(roleId);
            if (byId.isPresent()) {
                cycle = byId.get().getCycle();
            }
            for (int j = 0; j < result[0].length; j++) {
                //i:role    j:time
                Long employeeId = result[i][j];
                Optional<StationEmployee> byId1 = stationEmployeeRepository.findById(employeeId);
                if (!byId1.isPresent()) {
                    continue;
                }
                for (int k = 0; k < 7; k++) {
                    Date date = DateUtil.getNextDate(start, j * 7 + k);
                    if (canAdd(date, cycle)) {
                        RadioSchedule radioSchedule = new RadioSchedule();
                        radioSchedule.setDate(date);
                        radioSchedule.setEmployee(byId1.get());
                        radioSchedule.setRole(byId.get());

                        radioScheduleRepository.save(radioSchedule);
                    }
                }
            }
        }
    }

    private boolean canAdd(Date date, String cycle) {
        if (date.before(startDate) || date.after(endaDate)) {
            return false;
        }
        if (isHoliday(date)) {
            return false;
        }
        try {
            int dayOfWeek = DateUtil.getDayOfWeek(DateUtil.parseDateToString(date));
            char[] chars = cycle.toCharArray();
            if (chars[dayOfWeek - 1] == '1') {
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TODO: 2018/12/13 判断是否是节假日
    private boolean isHoliday(Date date) {
        return false;
    }

    // TODO: 2018/12/12 获取互斥的员工

    private ArrayList<Long> getMutexEmployee(long id) {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(id);
        return ids;
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
        startDate = DateUtil.parseDateString(from);
        endaDate = DateUtil.parseDateString(to);
        startWeek = DateUtil.getWeekOfYear(from);
        endWeek = DateUtil.getWeekOfYear(to);
        weekNums = endWeek - startWeek + 1;

        List<ProgramRole> programRoles = programRoleRepository.findAll();

        hasEmployeeProgramRoles = new ArrayList<>();
        for (int i = 0; i < programRoles.size(); i++) {
            //如果role没有员工 continue
            if (relationRoleAndEmployeeRepository.getAllByRoleId(programRoles.get(i).getId()).size() == 0) {
                continue;
            }
            hasEmployeeProgramRoles.add(programRoles.get(i));
        }

        //init result
        result = new Long[hasEmployeeProgramRoles.size()][weekNums];
        //init notOptionalInOneWeek
        notOptionalInOneWeek = new ArrayList<>();
        //init scheduleRoles
        scheduleRoles = new ArrayList<>();
        for (int i = 0; i < hasEmployeeProgramRoles.size(); i++) {
            ScheduleRoleWaitingList scheduleRole = new ScheduleRoleWaitingList();
            scheduleRole.init(programRoles.get(i), relationRoleAndEmployeeService, relationRoleAndEmployeeRepository);
            scheduleRoles.add(scheduleRole);
        }
    }
}
