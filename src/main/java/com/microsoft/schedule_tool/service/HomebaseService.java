package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.HomebaseType;
import com.microsoft.schedule_tool.entity.LateType;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface HomebaseService {

    List<HomebaseType> getAllHomebasesByAlias(String alias);

    HomebaseType saveHomebase(HomebaseType homebase);

    HomebaseType updateHomebase(Integer id, String homebaseDate, String comment, Boolean isNormal);

    boolean deleteHomebase(Integer id);
}
