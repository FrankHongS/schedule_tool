package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.exception.schedule.ProgramException;
import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.exception.schedule.ScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.*;
import com.microsoft.schedule_tool.schedule.domain.vo.schedule.ScheduleRoleWaitingList;
import com.microsoft.schedule_tool.schedule.repository.*;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
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

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private MutexEmployeeRepository mutexEmployeeRepository;

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

    //排班失败retry次数
    private static int RETRY_TIME = 15;
    private int currentTime = 0;

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

                                //找到了可以替换的员工
                                isChanged = true;
                                Long poll = alternativeEmployee.poll();
                                result[j][k] = poll;
                                result[j][i] = temp;
                                addToNotOptionalInOneWeek(poll);
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
            /*******************测试**/
            Long[][] test = new Long[result[0].length][result.length];
            for (int i = 0; i < result[0].length; i++) {
                for (int j = 0; j < result.length; j++) {
                    test[i][j] = result[j][i];
                }
            }
            /*******************测试**/
            //将排好的信息存入db
            saveData2Db();
            currentTime = 0;
        } catch (Exception e) {
            if (e instanceof ProgramScheduleException && currentTime < RETRY_TIME) {
                currentTime++;
                schedule(from, to);
            } else {
                throw new ProgramScheduleException(ResultEnum.SCHEDULE_ERROT_PLEASE_RETRY);
            }
        }
    }

    private void saveData2Db() {
        Date start = DateUtil.getFirstDayOfWeek(DateUtil.getYear(startDate), startWeek - 1);

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

    private boolean isHoliday(Date date) {
        List<Holiday> all = holidayRepository.findAll();
        if (all != null && all.size() > 0) {
            for (int i = 0; i < all.size(); i++) {
                Date date1 = all.get(i).getDate();
                if (DateUtil.isSameDay(date, date1)) {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO: 2018/12/12 获取互斥的员工

    private ArrayList<Long> getMutexEmployee(long id) {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(id);
        List<MutexEmployee> all = mutexEmployeeRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            String ids1 = all.get(i).getIds();
            boolean isInGroup = false;
            String[] split = ids1.split(",");
            for (int j = 0; j < split.length; j++) {
                if (id == Long.valueOf(split[j])) {
                    isInGroup = true;
                    break;
                }
            }
            if (isInGroup) {
                for (int j = 0; j < split.length; j++) {
                    if (Long.valueOf(split[j]) == id) {
                        continue;
                    }
                    ids.add(Long.valueOf(split[j]));
                }
            }
        }
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
        ArrayList<Long> mutexEmployee = getMutexEmployee(employeeId);
        notOptionalInOneWeek.addAll(mutexEmployee);
    }

    private void initParams(String from, String to) throws ParseException {
        startDate = DateUtil.parseDateString(from);
        endaDate = DateUtil.parseDateString(to);
        startWeek = DateUtil.getWeekOfYear(from);
        endWeek = DateUtil.getWeekOfYear(to);

        weekNums = (int) DateUtil.weeks(startDate, endaDate);

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
