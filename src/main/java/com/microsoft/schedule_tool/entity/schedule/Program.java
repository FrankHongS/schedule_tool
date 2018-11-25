package com.microsoft.schedule_tool.entity.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
@Entity(name = "tb_program")
public class Program {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false,name = "program_name")
    private String name;

    private Boolean workInWeekend;

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

    public Boolean getWorkInWeekend() {
        return workInWeekend;
    }

    public void setWorkInWeekend(Boolean workInWeekend) {
        this.workInWeekend = workInWeekend;
    }
}
