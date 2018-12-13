package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.ProgramRole;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/10
 **/
public interface ProgramRoleRepository extends JpaRepository<ProgramRole, Long> {
    Optional<ProgramRole> findByNameAndRadioProgram(String name, RadioProgram radioProgram);
    List<ProgramRole> findAllByRadioProgramAndIsDeleted(RadioProgram radioProgram,boolean isDeleted);

}
