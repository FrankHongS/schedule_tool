package com.microsoft.schedule_tool.entity.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
@Entity(name = "tb_program_employee")
public class ProgramEmployee {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    // 0 1~11, 1 A~C, 2 a~b
    @Column(nullable = false)
    private Integer employeeType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Integer employeeType) {
        this.employeeType = employeeType;
    }
}
