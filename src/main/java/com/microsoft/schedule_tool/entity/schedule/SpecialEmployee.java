package com.microsoft.schedule_tool.entity.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Frank Hon on 11/26/2018
 * E-mail: v-shhong@microsoft.com
 *
 * 不在同一个节目出现的员工
 */
@Entity(name = "tb_special")
public class SpecialEmployee {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

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
}
