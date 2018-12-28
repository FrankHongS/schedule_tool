package com.microsoft.schedule_tool.schedule.domain.vo.schedule;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RelationRoleAndEmployee;
import com.microsoft.schedule_tool.schedule.domain.entity.ScheduleStates;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespEmployeeByRoleId;
import com.microsoft.schedule_tool.schedule.repository.ProgramRoleRepository;
import com.microsoft.schedule_tool.schedule.repository.RelationRoleAndEmployeeRepository;
import com.microsoft.schedule_tool.schedule.repository.ScheduleStatesResposity;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author kb_jay
 * @time 2018/12/12
 **/
//角色对应的人员候选名单
public class ScheduleRoleWaitingList {


    public Long id;
    //最大权重
    public int maxRatio;

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
        // TODO: 2018/12/27
        for (int i = 0; i < allEmployee.size(); i++) {
            int ratio = allEmployee.get(i).getRatio();

        }
    }

    //初始化
    // TODO: 2018/12/25 查状态表初始化状态
    public void init(Long id, Date startDate, RelationRoleAndEmployeeService relationRoleAndEmployeeService, RelationRoleAndEmployeeRepository relationRoleAndEmployeeRepository, ScheduleStatesResposity scheduleStatesResposity, ProgramRoleRepository programRoleRepository) {
        //获取该角色下所有员工
        List<RespEmployeeByRoleId> employees = relationRoleAndEmployeeService.getAllWorkersByRoleId(id);
        //获取该角色下员工的权重最大值
        int maxRatio = 1;
        for (int j = 0; j < employees.size(); j++) {
            int ratio = relationRoleAndEmployeeService.getRatio(employees.get(j).getId(), id);
            maxRatio = maxRatio < ratio ? ratio : maxRatio;
        }

        this.id = id;
        this.maxRatio = maxRatio;
        //该角色下的（员工id+权重）数组
        //初始化allEmp，之后去掉已经排过的员工
        allEmployee = relationRoleAndEmployeeRepository.getAllByRoleId(id);
//        randomSort(allEmployee)
        for (int j = 0; j < allEmployee.size(); j++) {
            RelationRoleAndEmployee item = allEmployee.get(j);
            Long employeeId = item.getEmployeeId();
            int ratio = item.getRatio();
            for (int i = 0; i < ratio; i++) {
                allEmp.add(employeeId);
            }
        }
        //get first data from tb_shcdule_state by roleId and currentDate

        //if(has) -> 1:get has sorted employees from tb_schdule by isholidy=false and roleid and currentDate and firstDate

        //          2:init alternativeEmployee  ( allEmp except  has sorted)

        //else ->1:alternativeEmployee = allEmp


        

    }

    private void randomSort(List<RelationRoleAndEmployee> allEmployee) {
        int len = allEmployee.size();
        Random random = new Random();
        for (int i = 0; i < allEmployee.size(); i++) {
            int index = random.nextInt(len);
            RelationRoleAndEmployee temp = allEmployee.get(index);
            allEmployee.remove(index);
            allEmployee.add(temp);
            len--;
        }

    }
}
