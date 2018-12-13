package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.StationEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
public interface StationEmployeeRepository extends JpaRepository<StationEmployee, Long> {
    Optional<StationEmployee> findByAlias(String alias);

    List<StationEmployee> findAllByIsDeleted(boolean isDeleted);
}
