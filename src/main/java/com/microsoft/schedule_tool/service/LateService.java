package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.LateType;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface LateService {

    List<LateType> getAllLatesByAlias(String alias);

    LateType saveLate(LateType late);

    LateType updateLate(Integer id, String lateDate, String comment, Boolean isNormal);

    boolean deleteLate(Integer id);
}
