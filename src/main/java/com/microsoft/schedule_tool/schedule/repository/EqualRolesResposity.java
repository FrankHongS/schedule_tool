package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.EqualRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kb_jay
 * @time 2018/12/17
 **/
public interface EqualRolesResposity extends JpaRepository<EqualRole, Long> {

}
