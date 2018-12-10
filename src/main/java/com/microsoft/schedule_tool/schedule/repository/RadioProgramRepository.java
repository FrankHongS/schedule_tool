package com.microsoft.schedule_tool.schedule.repository;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import com.microsoft.schedule_tool.schedule.domain.entity.RadioStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
public interface RadioProgramRepository extends JpaRepository<RadioProgram, Long> {
    List<RadioProgram> findAllByRadioStationAndIsDeleted(RadioStation station, boolean isDeleted);
    Optional<RadioProgram> findByName(String name);
}
