package com.microsoft.schedule_tool.schedule.domain.vo.schedule;

import com.microsoft.schedule_tool.schedule.domain.entity.*;
import com.microsoft.schedule_tool.schedule.repository.*;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.util.DateUtil;

import java.util.*;

/**
 * @author kb_jay
 * @time 2018/12/12
 **/
//角色对应的人员候选名单
public class ScheduleRoleWaitingList {

    public Long id;

    public java.sql.Date firstDate;

    //备选员工
    public Queue<Long> alternativeEmployee = new LinkedList<>();

    //一轮所有的员工
    public ArrayList<Long> allEmp = new ArrayList<>();

    //所有员工
    public List<RelationRoleAndEmployee> allEmployee = new ArrayList<>();

    //更新角色候选名单
    public void updateAlternativeEmloyee() {
        if (!alternativeEmployee.isEmpty()) {
            return;
        }
        for (int i = 0; i < allEmp.size(); i++) {
            alternativeEmployee.offer(allEmp.get(i));
        }
    }

    //初始化
    public void init(Long id, Date startDate,
                     RelationRoleAndEmployeeService relationRoleAndEmployeeService,
                     RelationRoleAndEmployeeRepository relationRoleAndEmployeeRepository,
                     ScheduleStatesResposity scheduleStatesResposity,
                     ProgramRoleRepository programRoleRepository,
                     RadioScheduleRepository radioScheduleRepository) {
        this.id = id;

        initAllEmp(id, relationRoleAndEmployeeRepository);
        initAlternativeEmployeeAndFirstDate(id, startDate, scheduleStatesResposity, programRoleRepository, radioScheduleRepository);
    }

//    /**
//     * 初始化最大权重
//     *
//     * @param id
//     * @param relationRoleAndEmployeeService
//     * @return
//     */
//    private int initMaxRatio(Long id, RelationRoleAndEmployeeService relationRoleAndEmployeeService) {
//        //获取该角色下所有员工
//        List<RespEmployeeByRoleId> employees = relationRoleAndEmployeeService.getAllWorkersByRoleId(id);
//        //获取该角色下员工的权重最大值
//        int maxRatio = 1;
//        for (int j = 0; j < employees.size(); j++) {
//            int ratio = relationRoleAndEmployeeService.getRatio(employees.get(j).getId(), id);
//            maxRatio = maxRatio < ratio ? ratio : maxRatio;
//        }
//        return maxRatio;
//    }

    /**
     * 初始化一轮的emp
     *
     * @param id
     * @param relationRoleAndEmployeeRepository
     */
    private void initAllEmp(Long id, RelationRoleAndEmployeeRepository relationRoleAndEmployeeRepository) {
        //该角色下的（员工id+权重）数组
        //初始化allEmp，之后去掉已经排过的员工
        allEmployee = relationRoleAndEmployeeRepository.getAllByRoleId(id);
        for (int j = 0; j < allEmployee.size(); j++) {
            RelationRoleAndEmployee item = allEmployee.get(j);
            Long employeeId = item.getEmployeeId();
            int ratio = item.getRatio();
            for (int i = 0; i < ratio; i++) {
                allEmp.add(employeeId);
            }
        }
//        randomSort(allEmp);
    }

    /**
     * 初始化候选名单
     * //get first data from tb_shcdule_state by roleId and curDate
     * <p>
     * //if(has) -> 1:get has sorted employees from tb_schdule by isholidy=false and roleid and curDate and firstDate
     * <p>
     * //          2:init alternativeEmployee  ( allEmp except  has sorted)
     * <p>
     * //else ->1:alternativeEmployee = allEmp
     *
     * @param id
     * @param startDate
     * @param scheduleStatesResposity
     * @param programRoleRepository
     * @param radioScheduleRepository
     */
    private void initAlternativeEmployeeAndFirstDate(Long id, Date startDate, ScheduleStatesResposity scheduleStatesResposity, ProgramRoleRepository programRoleRepository, RadioScheduleRepository radioScheduleRepository) {
        ArrayList<Long> hasSortEmp = new ArrayList<>();

        Date thisWeekMonday = DateUtil.getThisWeekMonday(startDate);
        Optional<ProgramRole> roleOptional = programRoleRepository.findById(id);


        Optional<ScheduleStates> currentState = scheduleStatesResposity.getByCurDateAndRole(new java.sql.Date(thisWeekMonday.getTime()), roleOptional.get());
        if (currentState.isPresent()) {
            firstDate = currentState.get().getFirstDate();
            List<RadioSchedule> schedules = radioScheduleRepository.findAllByRoleAndDateLessThanEqualAndDateGreaterThanEqualAndIsHoliday(roleOptional.get(), startDate, firstDate, false);
            long currentId = -1;
            for (int i = 0; i < schedules.size(); i++) {
                Long id1 = schedules.get(i).getEmployee().getId();
                if (id1 != currentId) {
                    currentId = id1;
                    hasSortEmp.add(id1);
                }
            }
            ArrayList<Long> temp = allEmp;
            temp.removeAll(hasSortEmp);
            for (int i = 0; i < temp.size(); i++) {
                alternativeEmployee.offer(temp.get(i));
            }
        } else {
            for (int i = 0; i < allEmp.size(); i++) {
                alternativeEmployee.offer(allEmp.get(i));
            }
            firstDate = new java.sql.Date(thisWeekMonday.getTime());
        }
    }

    private <T> void randomSort(List<T> needRandArray) {
        int len = needRandArray.size();
        Random random = new Random();
        for (int i = 0; i < needRandArray.size(); i++) {
            int index = random.nextInt(len);
            T temp = needRandArray.get(index);
            needRandArray.remove(index);
            needRandArray.add(temp);
            len--;
        }

    }
}
