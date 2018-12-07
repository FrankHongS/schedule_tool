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
    @ManyToOne
    @JoinColumn(name = "role_id")
    private ProgramRole role;

    @Id
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private StationEmployee employee;

    //权重
    private double ratio;

    public RelationRoleAndEmployee() {
    }

    public RelationRoleAndEmployee(ProgramRole role, StationEmployee employee, double ratio) {
        this.role = role;
        this.employee = employee;
        this.ratio = ratio;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "RelationRoleAndEmployee{" +
                "role=" + role.toString() +
                ", employee=" + employee.toString() +
                ", ratio=" + ratio +
                '}';
    }
}
