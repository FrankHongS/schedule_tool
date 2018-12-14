package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.exception.schedule.ProgramScheduleException;
import com.microsoft.schedule_tool.schedule.domain.entity.MutexEmployee;
import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import com.microsoft.schedule_tool.schedule.domain.vo.response.MutextEmployeesResp;
import com.microsoft.schedule_tool.schedule.repository.MutexEmployeeRepository;
import com.microsoft.schedule_tool.schedule.repository.StationEmployeeRepository;
import com.microsoft.schedule_tool.schedule.service.MutexEmployeeService;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/14
 **/
@Service
public class MutexEmployeeServiceImpl implements MutexEmployeeService {
    @Autowired
    private MutexEmployeeRepository mutexEmployeeRepository;
    @Autowired
    private StationEmployeeRepository stationEmployeeRepository;

    @Override
    public List<MutextEmployeesResp> getAllMutexGroup() {
        try {
            List<MutextEmployeesResp> re = new ArrayList<>();
            List<MutexEmployee> all = mutexEmployeeRepository.findAll();
            for (int i = 0; i < all.size(); i++) {
                MutextEmployeesResp resp = new MutextEmployeesResp();
                resp.id = all.get(i).getId();
                String ids = all.get(i).getIds();
                List<MutextEmployeesResp.EmployeeResp> employeeResps = new ArrayList<>();
                String[] split = ids.split(",");
                if (split != null && split.length > 0) {
                    for (int j = 0; j < split.length; j++) {
                        Optional<StationEmployee> byId = stationEmployeeRepository.findById(Long.valueOf(split[j]));
                        if (byId.isPresent() && !byId.get().isDeleted()) {
                            MutextEmployeesResp.EmployeeResp employeeResp = new MutextEmployeesResp.EmployeeResp();
                            employeeResp.alias = byId.get().getAlias();
                            employeeResp.id = byId.get().getId();
                            employeeResp.name = byId.get().getName();
                            employeeResps.add(employeeResp);
                        }
                    }
                }
                resp.employees = employeeResps;
                re.add(resp);
            }
            return re;
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.MUTEX_FIND_FAILED);
        }
    }

    @Override
    public void addMutexEmployee(String ids) {
        if (ids == null) {
            throw new ProgramScheduleException(ResultEnum.MUTEX_IDS_NULL);
        }
        List<MutexEmployee> all = mutexEmployeeRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            String ids1 = all.get(i).getIds();
            if (ids.equals(ids1)) {
                return;
            }
        }
        // TODO: 2018/12/14 判断ids中的id是否合法：（存在且没有delete掉）
        try {
            MutexEmployee s = new MutexEmployee();
            s.setIds(ids);
            mutexEmployeeRepository.save(s);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.MUTEX_SAV_FAILED);
        }
    }

    @Override
    public void updateMutexEmployee(long id, String ids) {
        // TODO: 2018/12/14 判断ids中的id是否合法：（存在且没有delete掉）
        Optional<MutexEmployee> byId = mutexEmployeeRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ProgramScheduleException(ResultEnum.MUTEX_ID_NOT_EXIST);
        }
        if (ids == null) {
            throw new ProgramScheduleException(ResultEnum.MUTEX_IDS_NULL);
        }
        try {
            MutexEmployee mutexEmployee = byId.get();
            mutexEmployee.setIds(ids);
            mutexEmployeeRepository.saveAndFlush(mutexEmployee);
        } catch (Exception e) {
            throw new ProgramScheduleException(ResultEnum.MUTEX_UPDATE_FAILED);
        }
    }
}
