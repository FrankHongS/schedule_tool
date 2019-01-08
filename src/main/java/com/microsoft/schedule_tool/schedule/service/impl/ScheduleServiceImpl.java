package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.exception.schedule.ScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.*;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespSchedule;
import com.microsoft.schedule_tool.schedule.domain.vo.schedule.ScheduleRoleWaitingList;
import com.microsoft.schedule_tool.schedule.repository.*;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.util.ListUtils;
import com.microsoft.schedule_tool.util.LogUtils;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;
import sun.rmi.runtime.Log;

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

    @Autowired
    private EqualRolesResposity equalRolesResposity;

    @Autowired
    private ScheduleStatesResposity scheduleStatesResposity;

    private ArrayList<ScheduleRoleWaitingList> scheduleRoles;

    //一周中不可选的员工
    private ArrayList<Long> notOptionalInOneWeek;

    //跳过的角色（相同角色表）
    private HashMap<Long, Long> skipRoles;

    private int weekNums;

    private Long[][] result;
    private Date startDate;
    private Date endaDate;
    private int startWeek;
    private int endWeek;

    //排班失败retry次数
    private static int RETRY_TIME = 20;
    private int currentTime = 0;
    private HashSet<Long> needScheduleRole;
    private String special;
    private String special2;
    private boolean hasSortSuccess;

//    @Override
//    public void schedule(String from, String to) {
//        try {
//            //初始化排版参数：结果二维数组result；跳过排序的角色skipRole（hashmap）；周不可选；角色待选名单
//            initParams(from, to);
//            for (int i = 0; i < result[0].length; i++) {
//                for (int j = 0; j < result.length; j++) {
//                    //i->time  j->role
//                    //获取待选名单
//                    ScheduleRoleWaitingList scheduleRole = scheduleRoles.get(j);
//                    Queue<Long> alternativeEmployee = scheduleRole.alternativeEmployee;
//
//                    //选人，要求： 不在周不可选名单中 且 不可连续两周同一个人。
//                    int moveCount = 0;
//                    while (!alternativeEmployee.isEmpty()
//                            && moveCount < alternativeEmployee.size()
//                            && (isInNotOptionalInOneWeek(alternativeEmployee.peek()) || (i - 1 > 0 && alternativeEmployee.peek().equals(result[j][i - 1])))) {
//                        //不符合要求的人跟待选队列的最后一个换位置，接着检查队首的员工是否符合要求
//                        Long temp = alternativeEmployee.poll();
//                        alternativeEmployee.offer(temp);
//                        moveCount++;
//                    }
//
//                    if (moveCount == alternativeEmployee.size()) {
//                        //没有可选的人的时候从该角色前面已经排好的人中选一个跟他互换。
//                        //待选名单中的每一个人都试一遍
//                        int move = 0;
//                        boolean isChanged = false;
//                        while (!alternativeEmployee.isEmpty()
//                                && move < alternativeEmployee.size()
//                                && !isChanged) {
//                            Long employeeId = alternativeEmployee.peek();
//                            for (int k = 0; k < i; k++) {
//                                //1：选中的人不能在周不可选名单
//                                Long temp = result[j][k];
//                                if (notOptionalInOneWeek.contains(temp)) {
//                                    continue;
//                                }
//                                //2：被替换的元素左右不能是employeeId
//                                if (k - 1 >= 0 && result[j][k - 1].equals(employeeId)) {
//                                    continue;
//                                }
//                                if (k + 1 < i && result[j][k + 1].equals(employeeId)) {
//                                    continue;
//                                }
//
//                                //3：替换的那一列中不能有employeeId,并且不能有跟employeeId互斥的。
//                                boolean canChange = true;
//                                for (int l = 0; l < result.length; l++) {
//                                    if (employeeId.equals(result[l][k])
//                                            || getMutexEmployee(employeeId).contains(result[l][k])) {
//                                        canChange = false;
//                                        break;
//                                    }
//                                }
//                                if (!canChange) {
//                                    continue;
//                                }
//
//                                //*****找到了可以替换的员工
//                                isChanged = true;
//                                Long poll = alternativeEmployee.poll();
//                                result[j][k] = poll;
//                                result[j][i] = temp;
//                                addToNotOptionalInOneWeek(poll);
//                                updateScheduleState(i, j);
//                                break;
//                            }
//                            if (!isChanged) {
//                                Long temp = alternativeEmployee.poll();
//                                alternativeEmployee.offer(temp);
//                            }
//                            move++;
//                        }
//                        if (move == alternativeEmployee.size()) {
//                            //选不到人！！！！
//                            System.out.println("测试数据：第a次 第i周 j角色" + currentTime + " " + i + " " + j);
//                            throw new ProgramScheduleException(ResultEnum.SCHEDULE_ERROT_PLEASE_RETRY);
//                        }
//
//                    } else {
//                        //*****选到人了那么设置result值；更新待选名单；更新周不可选名单
//                        Long employeeId = alternativeEmployee.poll();
//                        result[j][i] = employeeId;
//                        scheduleRole.alternativeEmployee = alternativeEmployee;
//                        addToNotOptionalInOneWeek(employeeId);
//                        updateScheduleState(i, j);
//                    }
//                    if (alternativeEmployee.isEmpty()) {
//                        //同一个ratio排完了,更新候选名单
//                        Date nextDate = DateUtil.getNextDate(startDate, 7 * (i + 1));
//                        Date thisWeekMonday = DateUtil.getThisWeekMonday(nextDate);
//                        scheduleRole.updateAlternativeEmloyee(thisWeekMonday);
////                        updateScheduleState(i, j);
//                    }
//                }
//                //reset
//                notOptionalInOneWeek.clear();
//            }
//            /*******************测试**/
//            Long[][] test = new Long[result[0].length][result.length];
//            for (int i = 0; i < result[0].length; i++) {
//                for (int j = 0; j < result.length; j++) {
//                    test[i][j] = result[j][i];
//                }
//            }
//            /*******************测试**/
//            //清理掉旧数据
//            clearOldData(from, to);
//            //将排好的信息存入db
//            saveData2Db();
//            //清理掉可选员工表中to之后的信息
//            clearScheduleStateTo();
//
//        } catch (Exception e) {
//            try {
//                clearOldData(from, to);
//            } catch (ParseException e1) {
//                e1.printStackTrace();
//            }
//            clearScheduleState();
//            if (e instanceof ProgramScheduleException && currentTime < RETRY_TIME) {
//                currentTime++;
//                schedule(from, to);
//            } else {
//                throw new ProgramScheduleException(ResultEnum.SCHEDULE_ERROT_PLEASE_RETRY);
//            }
//        }
//    }


    private void clearScheduleStateTo(String from) throws ParseException {
        // TODO: 2018/12/24 清除掉状态信息
        Date dateFrom = DateUtil.parseDateString(from);

//        scheduleStatesResposity.deleteByStartDateGreaterThan((java.sql.Date) endaDate);
        scheduleStatesResposity.deleteByCurDateGreaterThan(new java.sql.Date(dateFrom.getTime()));
    }

    /**
     * 清除掉排班状态
     * todo
     */
    private void clearScheduleState() {
        scheduleStatesResposity.deleteByCurDateLessThanEqualAndCurDateGreaterThanEqual(new java.sql.Date(endaDate.getTime()), new java.sql.Date(DateUtil.getThisWeekMonday(startDate).getTime()));
    }

    /**
     * 更新数据库中排班状态
     *
     * @param i time
     * @param j role
     */
    private void updateScheduleState(int i, int j) {
        //1:get firstDate from rolesWaiting
        java.sql.Date firstDate = scheduleRoles.get(j).firstDate;
        Date mondayOfStartWeek = DateUtil.getThisWeekMonday(startDate);
        java.sql.Date curDate = new java.sql.Date(DateUtil.getNextDate(mondayOfStartWeek, i * 7).getTime());

        Optional<ProgramRole> roleOptional = programRoleRepository.findById(scheduleRoles.get(j).id);
        Optional<ScheduleStates> scheduleStatesOptional = scheduleStatesResposity.getByRoleAndCurDate(roleOptional.get(), curDate);
        if (scheduleStatesOptional.isPresent()) {
            ScheduleStates scheduleStates = scheduleStatesOptional.get();
            scheduleStates.setFirstDate(firstDate);
            scheduleStatesResposity.saveAndFlush(scheduleStates);
        } else {
            ScheduleStates scheduleStates = new ScheduleStates();
            scheduleStates.setCurDate(curDate);
            scheduleStates.setFirstDate(firstDate);
            scheduleStates.setRole(roleOptional.get());
            scheduleStatesResposity.saveAndFlush(scheduleStates);
        }
    }

    /**
     * 清掉数据库中的旧数据
     *
     * @param from
     */
    private void clearOldData(String from) throws ParseException {
        Date dateFrom = DateUtil.parseDateString(from);
//        Date dateTo = DateUtil.parseDateString(to);
//        radioScheduleRepository.deleteByDateLessThanEqualAndDateGreaterThanEqual(dateTo, dateFrom);
        radioScheduleRepository.deleteByDateGreaterThanEqual(dateFrom);
    }

    private void saveData2Db() throws ParseException {
        //单独处理国庆
//        if (containNational()) {
//            //获取国庆那一周
//            int nationalWeeknumber = DateUtil.getWeekOfYear(special);
//            int nationalWeekNumber2 = DateUtil.getWeekOfYear(special2);
//            if (nationalWeeknumber != nationalWeekNumber2) {
//                int index1 = nationalWeeknumber - startWeek;
//                int index2 = nationalWeekNumber2 - startWeek;
//                for (int i = 0; i < result.length; i++) {
//                    result[i][index2] = result[i][index1];
//                }
//            }
//        }

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
                        radioSchedule.setDate(new java.sql.Date(date.getTime()));
                        radioSchedule.setEmployee(byId1.get());
                        radioSchedule.setRole(byId.get());

                        radioScheduleRepository.save(radioSchedule);
                    }
                }
            }
        }
        //处理skipRoles
        for (Long key : skipRoles.keySet()) {
            Long roleId = skipRoles.get(key);
            int index = -1;
            for (Long item : needScheduleRole) {
                index++;
                if (roleId.equals(item)) {
                    break;
                }
            }
            Long[] s = result[index];

            String cycle = "1111100";
            Optional<ProgramRole> byId = programRoleRepository.findById(key);
            if (byId.isPresent()) {
                cycle = byId.get().getCycle();
            }
            for (int j = 0; j < s.length; j++) {
                //i:role    j:time
                Long employeeId = s[j];
                Optional<StationEmployee> byId1 = stationEmployeeRepository.findById(employeeId);
                if (!byId1.isPresent()) {
                    continue;
                }
                for (int k = 0; k < 7; k++) {
                    Date date = DateUtil.getNextDate(start, j * 7 + k);
                    if (canAdd(date, cycle)) {
                        RadioSchedule radioSchedule = new RadioSchedule();
                        radioSchedule.setDate(new java.sql.Date(date.getTime()));
                        radioSchedule.setEmployee(byId1.get());
                        radioSchedule.setRole(byId.get());

                        radioScheduleRepository.save(radioSchedule);
                    }
                }
            }


        }
    }

    private void handleHoliday() {
        //特殊处理7天假期
        List<Date> sevenHolidayEndDate = getSevenHolidayEndDate();
        for (int i = 0; i < sevenHolidayEndDate.size(); i++) {
            Date date = sevenHolidayEndDate.get(i);
            Date holidayStart = DateUtil.getNextDate(date, -6);
            if (containHoliday(holidayStart, date)) {
                int startToFrom = DateUtil.getBetweenWeeks(startDate, holidayStart);
                int endToFrom = DateUtil.getBetweenWeeks(startDate, date);
                if (startToFrom != endToFrom) {
                    for (int j = 0; j < result.length; j++) {
                        result[j][startToFrom] = result[j][endToFrom];
                    }
                }
            }
        }
    }

    private int getIndex() {

        return 0;
    }

    private boolean containHoliday(Date date1, Date date2) {
        return date1.getTime() >= startDate.getTime()
                && date2.getTime() <= endaDate.getTime();
    }

    private boolean containNational() throws ParseException {
        long startTime = startDate.getTime();
        int year = DateUtil.getYear(startDate);
        special = year + "-" + "10" + "-01";
        long time1 = DateUtil.parseDateString(special).getTime();
        special2 = year + "-" + "10" + "-07";
        long time2 = DateUtil.parseDateString(special2).getTime();
        long endTiem = endaDate.getTime();


        return time1 >= startTime
                && time2 <= endTiem;
    }

    /**
     * 获取7天假期的最后一天（list）
     * 处理假期
     *
     * @return
     */
    public List<Date> getSevenHolidayEndDate() {
        // TODO: 2018/12/29
        List<Holiday> all = holidayRepository.findAll();
        all.sort(new Comparator<Holiday>() {
            @Override
            public int compare(Holiday o1, Holiday o2) {
                return o1.getDate().getTime() > o2.getDate().getTime() ? 1 : -1;
            }
        });

        ArrayList<Date> dates = new ArrayList<>();

        int[] dp = new int[all.size()];
        Date pre = null;
        for (int i = 0; i < all.size(); i++) {
            if (i == 0) {
                dp[i] = 1;
                pre = all.get(i).getDate();
                continue;
            }
            Date cur = all.get(i).getDate();
            //相差一天
            if ((int) ((cur.getTime() - pre.getTime()) / (1000 * 3600 * 24) + 0.5f) == 1) {
                dp[i] = dp[i - 1] + 1;
            } else {
                dp[i] = 1;
            }
            pre = cur;
            if (dp[i] == 7) {
                dates.add(all.get(i).getDate());
            }
        }
        return dates;
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

    //  获取互斥的员工
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
    private void addToNotOptionalInOneWeek(long employeeId) {
//        ArrayList<Long> mutexEmployee = getMutexEmployee(employeeId);
//        notOptionalInOneWeek.addAll(mutexEmployee);
        notOptionalInOneWeek.add(employeeId);

        for (Long item : skipRoles.keySet()) {
            if (skipRoles.get(item).longValue() == employeeId) {
                notOptionalInOneWeek.add(skipRoles.get(item));
            }
        }
    }

    private void initParams(String from, String to) throws ParseException {
        startDate = DateUtil.parseDateString(from);
        endaDate = DateUtil.parseDateString(to);
        startWeek = DateUtil.getWeekOfYear(from);
        endWeek = DateUtil.getWeekOfYear(to);

        //获取间隔的周数（result的x维度）
        weekNums = (int) DateUtil.weeks(startDate, endaDate);

        //获取所有的角色（1：排除掉没有员工的角色，）
        List<ProgramRole> programRoles = programRoleRepository.findAllByIsDeleted(false);

        ArrayList<Long> hasEmployeeProgramRoles = new ArrayList<>();
        for (int i = 0; i < programRoles.size(); i++) {
            //如果role没有员工 continue
            if (relationRoleAndEmployeeRepository.getAllByRoleId(programRoles.get(i).getId()).size() == 0) {
                continue;
            }
            hasEmployeeProgramRoles.add(programRoles.get(i).getId());
        }
        //获取所有的角色（2：排除掉同一时间员工相同的角色）跳过的不需要排序的角色在skipRoles的keySet中。
        skipRoles = new HashMap<>();
        needScheduleRole = new HashSet<>();
        //不需要排序的角色
        HashMap<Long, Long> noNeedAdd = new HashMap<>();
        for (int i = 0; i < hasEmployeeProgramRoles.size(); i++) {
            Long id = hasEmployeeProgramRoles.get(i);
            if (noNeedAdd.keySet().contains(id)) {
                skipRoles.put(id, noNeedAdd.get(id));
            } else {
                needScheduleRole.add(id);
                checkSameRoles(id, noNeedAdd);
            }
        }
        //初始化result（两个维度：角色+周数）
        result = new Long[needScheduleRole.size()][weekNums];
        //同一周中不可选的员工
        notOptionalInOneWeek = new ArrayList<>();
        //初始化待选角色待选名单
        scheduleRoles = new ArrayList<>();
        for (Long id : needScheduleRole) {
            ScheduleRoleWaitingList scheduleRole = new ScheduleRoleWaitingList();
            scheduleRole.init(id, startDate, relationRoleAndEmployeeService, relationRoleAndEmployeeRepository, scheduleStatesResposity, programRoleRepository, radioScheduleRepository);
            scheduleRoles.add(scheduleRole);
        }
    }

    /**
     * 检查id，如果有相同的roleID，那么把跟他相同的roleId加入noNeedAdd(roleId->id)
     *
     * @param id
     * @param noNeedAdd
     */
    private void checkSameRoles(Long id, HashMap<Long, Long> noNeedAdd) {
        List<EqualRole> all = equalRolesResposity.findAll();
        for (int i = 0; i < all.size(); i++) {
            String ids = all.get(i).getIds();
            String[] split = ids.split(",");

            boolean isIn = false;

            for (int j = 0; j < split.length; j++) {
                if (id.equals(Long.valueOf(split[j]))) {
                    isIn = true;
                    break;
                }
            }
            if (isIn) {
                for (int j = 0; j < split.length; j++) {
                    if (Long.valueOf(split[j]).equals(id)) {
                        continue;
                    }
                    noNeedAdd.put(Long.valueOf(split[j]), id);
                }
            }
        }
    }

    @Override
    public void addHolidayEmployee(String date, long roleId, long empoyeeId) {
        Optional<ProgramRole> roleOptional = programRoleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.PROGRAM_ROLE_ID_NOT_EXIST);
        }
        String cycle = roleOptional.get().getCycle();
        //check params
        Date date1;
        try {
            date1 = DateUtil.parseDateString(date);
            if (!isHoliday(date1) && cycle.charAt(DateUtil.getDayOfWeek(date) - 1) != '0') {
                throw new ProgramScheduleException(ResultEnum.SCHEDULE_HOLIDAY_DATE_NOT_HOLIDAY);
            }
        } catch (ParseException e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_HOLIDAY_DATE_PARSE_ERROR);
        }
        Optional<StationEmployee> employeeOptional = stationEmployeeRepository.findById(empoyeeId);
        if (!employeeOptional.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.EMPLOYEE_ID_NOT_EXIST);
        }
        Optional<RadioSchedule> byDateAndAndRole = radioScheduleRepository.findByDateAndRole(date1, roleOptional.get());
        if (byDateAndAndRole.isPresent()) {
            RadioSchedule radioSchedule = byDateAndAndRole.get();
            radioSchedule.setEmployee(employeeOptional.get());
            radioScheduleRepository.saveAndFlush(radioSchedule);
            return;
        }
        try {
            RadioSchedule radioSchedule = new RadioSchedule();
            radioSchedule.setRole(roleOptional.get());
            radioSchedule.setEmployee(employeeOptional.get());
            radioSchedule.setDate(new java.sql.Date(date1.getTime()));
            radioSchedule.setHoliday(true);
            radioScheduleRepository.save(radioSchedule);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_HOLIDAY_SAVE_FAILED);
        }
    }

    @Override
    public void deleteHolidaySchedule(long id) {
        Optional<RadioSchedule> radioScheduleOptional = radioScheduleRepository.findById(id);
        if (!radioScheduleOptional.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_HOLIDAY_ID_NOT_EXTST);
        }
        Date date = radioScheduleOptional.get().getDate();
        if (!isHoliday(date)) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_HOLIDAY_DATE_NOT_HOLIDAY);
        }
        try {
            radioScheduleRepository.deleteById(id);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_HOLIDAY_DELETE_FAILED);
        }
    }

    @Override
    public List<RespSchedule> getAllSchedule(String from, String to, boolean isHoliday) {
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = DateUtil.parseDateString(from);
            toDate = DateUtil.parseDateString(to);
        } catch (ParseException e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_DATE_PARSE_ERROR);
        }
        try {
            List<RadioSchedule> radioSchedules = radioScheduleRepository.findAllByDateLessThanEqualAndDateGreaterThanEqualAndIsHoliday(toDate, fromDate, isHoliday);
            List<RespSchedule> re = new ArrayList<>();
            for (int i = 0; i < radioSchedules.size(); i++) {
                RadioSchedule radioSchedule = radioSchedules.get(i);
                RespSchedule respSchedule = new RespSchedule();
                respSchedule.id = radioSchedule.getId();
                respSchedule.alias = radioSchedule.getEmployee().getAlias();
                respSchedule.name = radioSchedule.getEmployee().getName();
                respSchedule.date = DateUtil.parseDateToString(radioSchedule.getDate());
                respSchedule.programName = radioSchedule.getRole().getRadioProgram().getName();
                respSchedule.roleName = radioSchedule.getRole().getName();
                re.add(respSchedule);
            }
            return re;
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_FIND_FAILED);
        }
    }

    @Override
    public List<RespSchedule> getHolidaySchedule() {
        try {
            List<RadioSchedule> radioSchedules = radioScheduleRepository.findAllByIsHoliday(true);
            List<RespSchedule> re = new ArrayList<>();
            for (int i = 0; i < radioSchedules.size(); i++) {
                RadioSchedule radioSchedule = radioSchedules.get(i);
                RespSchedule respSchedule = new RespSchedule();
                respSchedule.id = radioSchedule.getId();
                respSchedule.alias = radioSchedule.getEmployee().getAlias();
                respSchedule.name = radioSchedule.getEmployee().getName();
                respSchedule.date = DateUtil.parseDateToString(radioSchedule.getDate());
                respSchedule.programName = radioSchedule.getRole().getRadioProgram().getName();
                respSchedule.roleName = radioSchedule.getRole().getName();
                re.add(respSchedule);
            }
            return re;
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_FIND_FAILED);
        }
    }

    /**
     * 排班
     *
     * @param from
     * @param to
     */
    @Override
    public void schedule(String from, String to) {
        try {
            initParams(from, to);
            LogUtils.getInstance().write("start-time" + DateUtil.parseDateToString(new Date()));
            schedule(0, 0);
            LogUtils.getInstance().write("end-time success:" + DateUtil.parseDateToString(new Date()));
            //清理掉旧数据
            clearOldData(from);
            clearScheduleStateTo(from);
            //处理假期
            handleHoliday();
            saveData2Db();
            saveState2Db();

        } catch (Exception e) {
            LogUtils.getInstance().write("end-time failed:" + DateUtil.parseDateToString(new Date()));
            throw new ProgramScheduleException(ResultEnum.SCHEDULE_FAILED);
        }

    }

    /**
     * 根据result存放状态
     */
    private void saveState2Db() {
        if (result != null && result.length > 0 && result[0].length > 0) {
            Date mondayOfStartWeek = DateUtil.getThisWeekMonday(startDate);
            for (int i = 0; i < result.length; i++) {
                Long roleId = scheduleRoles.get(i).id;
                //找到跳过的array
                ArrayList<Long> skip = new ArrayList<>();
                for (Long id : skipRoles.keySet()) {
                    Long aLong = skipRoles.get(id);
                    if (aLong.longValue() == roleId.longValue()) {
                        skip.add(id);
                    }
                }


                Optional<ProgramRole> roleOptional = programRoleRepository.findById(roleId);
                int allSize = scheduleRoles.get(i).allEmp.size();
                int alterSize = scheduleRoles.get(i).alternativeEmployee.size();
                for (int j = 0; j < result[0].length; j++) {
                    java.sql.Date curDate = new java.sql.Date(DateUtil.getNextDate(mondayOfStartWeek, j * 7).getTime());

                    ScheduleStates scheduleStates = new ScheduleStates();
                    scheduleStates.setRole(roleOptional.get());
                    scheduleStates.setCurDate(curDate);
                    //有状态
                    if (alterSize < allSize) {
                        if (j < alterSize) {
                            scheduleStates.setFirstDate(scheduleRoles.get(i).firstDate);
                        } else {
                            Date date = DateUtil.getNextDate(mondayOfStartWeek, 7 * alterSize);
                            int count = (j - alterSize) / allSize;
                            java.sql.Date date1 = new java.sql.Date(DateUtil.getNextDate(date, count * 7 * allSize).getTime());
                            scheduleStates.setFirstDate(date1);
                        }
                    } else {
                        int count = j / allSize;
                        java.sql.Date date = new java.sql.Date(DateUtil.getNextDate(mondayOfStartWeek, count * 7 * allSize).getTime());
                        scheduleStates.setFirstDate(date);
                    }
                    scheduleStatesResposity.saveAndFlush(scheduleStates);


                    //处理skip
                    for (int k = 0; k < skip.size(); k++) {
                        Optional<ProgramRole> byId = programRoleRepository.findById(skip.get(k));


                        ScheduleStates skipStates = new ScheduleStates();
                        skipStates.setFirstDate(scheduleStates.getFirstDate());
                        skipStates.setCurDate(scheduleStates.getCurDate());
                        skipStates.setRole(byId.get());
                        scheduleStatesResposity.saveAndFlush(skipStates);
                    }

                }
            }

        }
    }

    private void schedule(int i, int j) {
        while (true) {
            if (ok(i, j)) {
                if (i == needScheduleRole.size() - 1) {
                    i = 0;
                    j = j + 1;
                    if (j == weekNums) {
                        System.out.println("******成功*******");
                        break;
                    }
                } else {
                    i = i + 1;
                }
            } else {
                if (i == 0) {
                    if (j == 0) {
                        System.out.println("------失败------");
                        throw new ProgramScheduleException(ResultEnum.SCHEDULE_FAILED);
                    } else {
                        i = needScheduleRole.size() - 1;
                        j = j - 1;
                    }
                } else {
                    i = i - 1;
                }
            }
        }
    }

    /**
     * 递归回溯（栈深会stackoverflow）
     *
     * @param i 角色
     * @param j week
     * @deprecated
     */
    private void schedule1(int i, int j) {
        if (i == 0 && j == weekNums) {
            //成功
            System.out.println("********成功**********");
            return;
        }
        if (ok(i, j)) {
            if (i == needScheduleRole.size() - 1) {
                schedule1(0, j + 1);
            } else {
                schedule1(i + 1, j);
            }
        } else {
            if (i == 0) {
                if (j == 0) {                  //失败
                    System.out.println("---------失败----------");
                    return;
                } else {
                    schedule1(needScheduleRole.size() - 1, j - 1);
                }
            } else {
                schedule1(i - 1, j);
            }
        }
    }

    private boolean ok(int i, int j) {
        System.out.println("----------->>");
        System.out.println("排" + i + "->" + j + "角色id：" + scheduleRoles.get(i).id);
        LogUtils.getInstance().write("----------->>\n");
        LogUtils.getInstance().write("排" + i + "->" + j + "角色id：" + scheduleRoles.get(i).id + "\n");

        notOptionalInOneWeek.clear();
        for (int k = 0; k < i; k++) {
            if (result[k][j] != null) {
                addToNotOptionalInOneWeek(result[k][j]);
            }
        }


        ArrayList<Long> initAlternativeEmployees = new ArrayList<>();
        Queue<Long> alternativeEmployee = scheduleRoles.get(i).alternativeEmployee;
        for (int k = 0; k < alternativeEmployee.size(); k++) {
            Long poll = alternativeEmployee.poll();
            initAlternativeEmployees.add(poll);
            alternativeEmployee.offer(poll);
        }
        ArrayList<Long> allEmployees = scheduleRoles.get(i).allEmp;

        ArrayList<Long> canChooseEmployees = new ArrayList<>();
        ArrayList<Long> hasUseEmployees = new ArrayList<>();
        int initEmployeeSize = initAlternativeEmployees.size();
        if (j < initEmployeeSize) {
            canChooseEmployees.clear();
            canChooseEmployees.addAll(initAlternativeEmployees);
            for (int k = 0; k < j; k++) {
                if (result[i][k] != null) {
                    hasUseEmployees.add(result[i][k]);
                }
            }
        } else {
            canChooseEmployees.clear();
            canChooseEmployees.addAll(allEmployees);
            int start = (j - initEmployeeSize) / allEmployees.size() * allEmployees.size() + initEmployeeSize;
            for (int k = start; k < j; k++) {
                if (result[i][k] != null) {
                    hasUseEmployees.add(result[i][k]);
                }
            }
        }
        // TODO: 2019/1/8
        System.out.print("待选员工" + canChooseEmployees.size() + "->>");
        LogUtils.getInstance().write("待选员工" + canChooseEmployees.size() + "->>");
        for (int k = 0; k < canChooseEmployees.size(); k++) {
            System.out.print(canChooseEmployees.get(k) + "-");
            LogUtils.getInstance().write(canChooseEmployees.get(k) + "-");
        }
        System.out.println();
        LogUtils.getInstance().write("\n");
        ListUtils.removeAll(canChooseEmployees, hasUseEmployees);
        System.out.print("已经用过的员工" + hasUseEmployees.size() + "->>");
        LogUtils.getInstance().write("已经用过的员工" + hasUseEmployees.size() + "->>");
        for (int k = 0; k < hasUseEmployees.size(); k++) {
            System.out.print(hasUseEmployees.get(k) + "-");
            LogUtils.getInstance().write(hasUseEmployees.get(k) + "-");

        }
        System.out.println();
        LogUtils.getInstance().write("\n");
        canChooseEmployees.removeAll(notOptionalInOneWeek);

        LogUtils.getInstance().write("一周不可选员工" + notOptionalInOneWeek.size() + "->>");
        System.out.print("一周不可选员工" + notOptionalInOneWeek.size() + "->>");
        for (int k = 0; k < notOptionalInOneWeek.size(); k++) {
            System.out.print(notOptionalInOneWeek.get(k) + "-");
            LogUtils.getInstance().write(notOptionalInOneWeek.get(k) + "-");
        }
        System.out.println();

//        canChooseEmployees.removeAll(hasUseEmployees);
//        canChooseEmployees.removeAll(notOptionalInOneWeek);
        if (j > 0) {
            Long o = result[i][j - 1];
            Iterator<Long> iterator = canChooseEmployees.iterator();
            while (iterator.hasNext()) {
                Long next = iterator.next();
                if (next.longValue() == o.longValue()) {
                    iterator.remove();
                }
            }
//            canChooseEmployees.remove(o);
        }

        LogUtils.getInstance().write("\n");
        LogUtils.getInstance().write("可选员工不去重" + canChooseEmployees.size() + "->>");

        System.out.print("可选员工不去重" + canChooseEmployees.size() + "->>");
        for (int k = 0; k < canChooseEmployees.size(); k++) {
            System.out.print(canChooseEmployees.get(k) + "-");
            LogUtils.getInstance().write(canChooseEmployees.get(k) + "-");

        }
        LogUtils.getInstance().write("\n");

        System.out.println();

        //canChooseEmployees不能有重复的员工！！
        HashSet<Long> canChooseEmployeesSet = new HashSet<>();
        for (int k = 0; k < canChooseEmployees.size(); k++) {
            canChooseEmployeesSet.add(canChooseEmployees.get(k));
        }

        ArrayList<Long> canChooseEmployeesArray = new ArrayList<>();
        LogUtils.getInstance().write("可选员工去重" + canChooseEmployeesSet.size() + "->>");

        System.out.print("可选员工去重" + canChooseEmployeesSet.size() + "->>");
        for (Long id : canChooseEmployeesSet) {
            canChooseEmployeesArray.add(id);
            System.out.print(id + "-");
            LogUtils.getInstance().write(id + "-");

        }
        LogUtils.getInstance().write("\n");

        System.out.println();

        if (canChooseEmployeesArray.size() == 0) {
            System.out.println("失败");
            LogUtils.getInstance().write("失败\n");

            return false;
        } else {
            Long pre = result[i][j];
            if (pre == null) {
                result[i][j] = canChooseEmployeesArray.get(0);
                System.out.println("成功->>" + result[i][j]);
                LogUtils.getInstance().write("成功->>" + result[i][j] + "\n");

                return true;
            } else {
                boolean has = false;
                for (int k = 0; k < canChooseEmployeesArray.size(); k++) {
                    if (has) {
                        result[i][j] = canChooseEmployeesArray.get(k);
                        System.out.println("成功->>" + result[i][j]);
                        LogUtils.getInstance().write("成功->>" + result[i][j] + "\n");

                        return true;
                    }
                    if (pre.longValue() == canChooseEmployeesArray.get(k).longValue()) {
                        has = true;
                    }
                }
                result[i][j] = null;
                System.out.println("失败");
                LogUtils.getInstance().write("失败\n");

                return false;
            }
        }
    }
}
