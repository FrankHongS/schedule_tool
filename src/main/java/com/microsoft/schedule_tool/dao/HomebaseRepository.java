package com.microsoft.schedule_tool.dao;

import com.microsoft.schedule_tool.entity.HomebaseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface HomebaseRepository extends JpaRepository<HomebaseType,Integer> {

    List<HomebaseType> findByAlias(String alias);
}
