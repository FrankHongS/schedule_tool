package com.microsoft.schedule_tool.dao;

import com.microsoft.schedule_tool.entity.LateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface LateRepository extends JpaRepository<LateType,Integer> {

    List<LateType> findByAlias(String alias);
}
