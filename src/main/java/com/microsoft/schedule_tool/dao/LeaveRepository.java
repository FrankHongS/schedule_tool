package com.microsoft.schedule_tool.dao;

import com.microsoft.schedule_tool.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */
public interface LeaveRepository extends JpaRepository<LeaveType,Integer> {

    List<LeaveType> findByAlias(String alias);
}
