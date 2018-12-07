package com.microsoft.schedule_tool.schedule.domain.entity;

import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
public class RoleEmployeeMultiKeysClass implements Serializable {

    private ProgramRole role;
    private StationEmployee employee;

    public RoleEmployeeMultiKeysClass() {
    }

    public ProgramRole getRole() {
        return role;
    }

    public void setRole(ProgramRole role) {
        this.role = role;
    }

    public StationEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(StationEmployee employee) {
        this.employee = employee;
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
        if (role == null) {
            if (temp.role != null) {
                return false;
            }
        } else if (!role.equals(temp.role)) {
            return false;
        }

        if (employee == null) {
            if (temp.employee != null) {
                return false;
            }
        } else if (!employee.equals(temp.employee)) {
            return false;
        }

        return true;
    }
}
