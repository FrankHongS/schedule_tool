package com.microsoft.schedule_tool.schedule.domain.vo.schedule;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RelationRoleAndEmployee;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespEmployeeByRoleId;
import com.microsoft.schedule_tool.schedule.repository.RelationRoleAndEmployeeRepository;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
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
    //当前选到那个权重的员工了
    public int currentRatio;
    //备选员工
    public Queue<Long> alternativeEmployee = new LinkedList<>();
    //所有员工
    public List<RelationRoleAndEmployee> allEmployee = new ArrayList<>();


    //更新角色候选名单
    public void updateAlternativeEmloyee() {
        if (!alternativeEmployee.isEmpty()) {
            return;
        }
        if (currentRatio < maxRatio) {
        } else {
            currentRatio = 1;
        }
        for (int i = 0; i < allEmployee.size(); i++) {
            int ratio = allEmployee.get(i).getRatio();
            if (ratio <= currentRatio) {
                alternativeEmployee.offer(allEmployee.get(i).getEmployeeId());
            }
        }
    }

    //初始化
    public void init(Long id, RelationRoleAndEmployeeService relationRoleAndEmployeeService, RelationRoleAndEmployeeRepository relationRoleAndEmployeeRepository) {
        //获取该角色下所有员工
        List<RespEmployeeByRoleId> employees = relationRoleAndEmployeeService.getAllWorkersByRoleId(id);
        //获取该角色下员工的权重最大值
        int maxRatio = 1;
        for (int j = 0; j < employees.size(); j++) {
            int ratio = relationRoleAndEmployeeService.getRatio(employees.get(j).getId(), id);
            maxRatio = maxRatio < ratio ? ratio : maxRatio;
        }

        this.id = id;
        this.currentRatio = 1;
        this.maxRatio = maxRatio;
        //该角色下的（员工id+权重）数组
        allEmployee = relationRoleAndEmployeeRepository.getAllByRoleId(id);
//        randomSort(allEmployee);
        //获取所有权重为1的员工初始化待选名单
        for (int j = 0; j < allEmployee.size(); j++) {
            int ratio = allEmployee.get(j).getRatio();
            if (currentRatio == ratio) {
                alternativeEmployee.offer(allEmployee.get(j).getEmployeeId());
            }
        }
        //更新当前权重
        currentRatio++;
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
