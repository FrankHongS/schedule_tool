package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.entity.Late;

import java.util.List;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface LateService {

    List<Late> getAllLatesByAlias(String alias);

    Late saveLate(Late late);

    Late updateLate(Integer id, Integer lateType, String lateDate, String comment, Boolean isNormal);

    boolean deleteLate(Integer id);
}
