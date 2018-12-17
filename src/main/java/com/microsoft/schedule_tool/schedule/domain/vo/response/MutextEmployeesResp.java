package com.microsoft.schedule_tool.schedule.domain.vo.response;

import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/14
 **/
public class MutextEmployeesResp {
    public long id;
    public List<EmployeeResp> employees;

    public static class EmployeeResp {
        public long id;
        public String name;
        public String alias;
    }
}
