package com.microsoft.schedule_tool.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */

@Entity(name = "tb_leave")
public class Leave {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer leaveType;

    @Column(nullable = false)
    private String leaveDateRange;

    @Column(nullable = false)
    private Integer dayCount;

    private String comment;

    private Boolean isNormal;

    @Column(nullable = false)
    private Integer employeeId;

    @CreationTimestamp
    private Date createdTime;

    @UpdateTimestamp
    private Date lastUpdatedTime;

    public Leave(){}

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

    public Integer getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveDateRange() {
        return leaveDateRange;
    }

    public void setLeaveDateRange(String leaveDateRange) {
        this.leaveDateRange = leaveDateRange;
    }

    public Integer getDayCount() {
        return dayCount;
    }

    public void setDayCount(Integer dayCount) {
        this.dayCount = dayCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getNormal() {
        return isNormal;
    }

    public void setNormal(Boolean normal) {
        isNormal = normal;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
