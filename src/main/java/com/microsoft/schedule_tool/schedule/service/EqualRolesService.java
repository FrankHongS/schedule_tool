package com.microsoft.schedule_tool.schedule.service;

import com.microsoft.schedule_tool.schedule.domain.vo.response.EquelRoleResp;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/17
 **/
public interface EqualRolesService {
    List<EquelRoleResp> getAllEqualRolesGroup();

    void addEqualRoles(String ids);

    void updateEqualRoles(long id, String ids);
}
