package com.microsoft.schedule_tool.schedule.domain.vo.response;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/17
 **/
public class EquelRoleResp {
    public long id;
    public List<Roles> rolesList;

    public static class Roles {
        //1:角色id  2：节目名称  3：角色名称
        public long id;
        public String programName;
        public String roleName;
    }
}
