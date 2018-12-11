package com.microsoft.schedule_tool.schedule.domain.entity;

import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
public class RoleEmployeeMultiKeysClass implements Serializable {

    private Long roleId;
    private Long employeeId;

    public RoleEmployeeMultiKeysClass() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RoleEmployeeMultiKeysClass)) {
            return false;
        }

        RoleEmployeeMultiKeysClass temp = (RoleEmployeeMultiKeysClass) obj;
        if (roleId != temp.roleId) {
            return false;
        }

        if (employeeId != temp.employeeId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((roleId == null) ? 0 : roleId.hashCode());
        result = PRIME * result + ((employeeId == null) ? 0 : employeeId.hashCode());
        return result;
    }
}
