package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.*;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespSchedule;
import com.microsoft.schedule_tool.schedule.domain.vo.schedule.ScheduleRoleWaitingList;
import com.microsoft.schedule_tool.schedule.repository.*;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

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

    @Override
    public void schedule(String from, String to) {
        try {
            //初始化排版参数：结果二维数组result；跳过排序的角色skipRole（hashmap）；周不可选；角色待选名单
            initParams(from, to);
            for (int i = 0; i < result[0].length; i++) {
                for (int j = 0; j < result.length; j++) {
                    //i->time  j->role
                    //获取待选名单
                    ScheduleRoleWaitingList scheduleRole = scheduleRoles.get(j);
                    Queue<Long> alternativeEmployee = scheduleRole.alternativeEmployee;

                    //选人，要求： 不在周不可选名单中 且 不可连续两周同一个人。
                    int moveCount = 0;
                    while (!alternativeEmployee.isEmpty()
                            && moveCount < alternativeEmployee.size()
                            && (isInNotOptionalInOneWeek(alternativeEmployee.peek()) || (i - 1 > 0 && alternativeEmployee.peek().equals(result[j][i - 1])))) {
                        //不符合要求的人跟待选队列的最后一个换位置，接着检查队首的员工是否符合要求
                        Long temp = alternativeEmployee.poll();
                        alternativeEmployee.offer(temp);
                        moveCount++;
                    }

                    if (moveCount == alternativeEmployee.size()) {
                        //没有可选的人的时候从该角色前面已经排好的人中选一个跟他互换。
                        //待选名单中的每一个人都试一遍
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

                                //*****找到了可以替换的员工
                                isChanged = true;
                                Long poll = alternativeEmployee.poll();
                                result[j][k] = poll;
                                result[j][i] = temp;
                                addToNotOptionalInOneWeek(poll);
                                updateScheduleState(i, j);
                                break;
                            }
                            if (!isChanged) {
                                Long temp = alternativeEmployee.poll();
                                alternativeEmployee.offer(temp);
                            }
                            move++;
                        }
                        if (move == alternativeEmployee.size()) {
                            //选不到人！！！！
                            System.out.println("测试数据：第a次 第i周 j角色" + currentTime + " " + i + " " + j);
                            throw new ProgramScheduleException(ResultEnum.SCHEDULE_ERROT_PLEASE_RETRY);
                        }

                    } else {
                        //*****选到人了那么设置result值；更新待选名单；更新周不可选名单
                        Long employeeId = alternativeEmployee.poll();
                        result[j][i] = employeeId;
                        scheduleRole.alternativeEmployee = alternativeEmployee;
                        addToNotOptionalInOneWeek(employeeId);
                        updateScheduleState(i, j);
                    }
                    if (alternativeEmployee.isEmpty()) {
                        //同一个ratio排完了,更新候选名单
                        Date nextDate = DateUtil.getNextDate(startDate, 7 * (i + 1));
                        Date thisWeekMonday = DateUtil.getThisWeekMonday(nextDate);
                        scheduleRole.updateAlternativeEmloyee(thisWeekMonday);
//                        updateScheduleState(i, j);
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
            //清理掉旧数据
            clearOldData(from, to);
            //将排好的信息存入db
            saveData2Db();
            //清理掉可选员工表中to之后的信息
            clearScheduleStateTo();

        } catch (Exception e) {
            try {
                clearOldData(from, to);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            clearScheduleState();
            if (e instanceof ProgramScheduleException && currentTime < RETRY_TIME) {
                currentTime++;
                schedule(from, to);
            } else {
                throw new ProgramScheduleException(ResultEnum.SCHEDULE_ERROT_PLEASE_RETRY);
            }
        }
    }

    private void clearScheduleStateTo() {
        // TODO: 2018/12/24 清除掉to之后的数据
//        scheduleStatesResposity.deleteByStartDateGreaterThan((java.sql.Date) endaDate);
        scheduleStatesResposity.deleteByCurDateGreaterThan(new java.sql.Date(endaDate.getTime()));
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
     * @param to
     */
    private void clearOldData(String from, String to) throws ParseException {
        Date dateFrom = DateUtil.parseDateString(from);
        Date dateTo = DateUtil.parseDateString(to);
        radioScheduleRepository.deleteByDateLessThanEqualAndDateGreaterThanEqual(dateTo, dateFrom);
    }

    private void saveData2Db() throws ParseException {

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
        ArrayList<Long> mutexEmployee = getMutexEmployee(employeeId);
        notOptionalInOneWeek.addAll(mutexEmployee);
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
}
