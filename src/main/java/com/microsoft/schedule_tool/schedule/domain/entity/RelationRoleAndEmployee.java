package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_role_employee")
@IdClass(RoleEmployeeMultiKeysClass.class)
public class RelationRoleAndEmployee implements Serializable {
    private static final long serialVersionUID = 1918446199175160460L;

    @Id
    private Long roleId;

    @Id
    private Long employeeId;

    //权重
    private double ratio;

    public RelationRoleAndEmployee() {
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "RelationRoleAndEmployee{" +
                ", ratio=" + ratio +
                '}';
    }

}
