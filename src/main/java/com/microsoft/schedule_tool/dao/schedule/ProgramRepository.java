package com.microsoft.schedule_tool.dao.schedule;

import com.microsoft.schedule_tool.entity.schedule.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface ProgramRepository extends JpaRepository<Program,Integer> {

    Optional<Program> findByName(String name);
}
