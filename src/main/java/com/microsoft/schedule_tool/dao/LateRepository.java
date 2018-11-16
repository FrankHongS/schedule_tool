package com.microsoft.schedule_tool.dao;

import com.microsoft.schedule_tool.entity.Late;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface LateRepository extends JpaRepository<Late,Integer> {

    List<Late> findByAlias(String alias);

    List<Late> findByEmployeeId(Integer employeeId);

    List<Late> findByEmployeeIdAndLateType(Integer employeeId,Integer LateType);

    List<Late> findByCreatedTimeAfterAndCreatedTimeBeforeAndAlias(Date from,Date to,String alias);

    List<Late> findByCreatedTimeAfterAndCreatedTimeBefore(Date from,Date to);

    List<Late> findByLateDateAfterAndLateDateBeforeAndAlias(Date from,Date to,String alias);

    List<Late> findByLateDateAfterAndLateDateBefore(Date from,Date to);

    List<Late> findByLateDateIsAfterAndLateDateBeforeAndEmployeeIdAndLateType(Date from, Date to, Integer employeeId,Integer lateType);

    List<Late> findByLateDateIsAfterAndLateDateBeforeAndEmployeeId(Date from, Date to, Integer employeeId);

    void deleteByEmployeeId(Integer employeeId);
}
