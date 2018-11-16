package com.microsoft.schedule_tool.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Frank Hon on 2018/11/5
 * E-mail: frank_hon@foxmail.com
 */
@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false,unique = true)
    private String alias;

    @Column(nullable = false)
    private String name;

    private Float annual;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date lastUpdatedTime;

    public Employee(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAnnual() {
        return annual;
    }

    public void setAnnual(Float annual) {
        this.annual = annual;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
