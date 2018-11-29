package com.microsoft.schedule_tool.entity.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Frank Hon on 11/29/2018
 * E-mail: v-shhong@microsoft.com
 */
@Entity(name = "tb_employee_index")
public class ScheduleEmployeeIndex {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "employee_index",nullable = false)
    private Integer index;

    @Column(nullable = false)
    private Integer employeeType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Integer employeeType) {
        this.employeeType = employeeType;
    }
}
