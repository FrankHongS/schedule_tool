package com.microsoft.schedule_tool.entity.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Frank Hon on 11/27/2018
 * E-mail: v-shhong@microsoft.com
 */
@Entity(name = "tb_substitute")
public class Substitute {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Date substituteDate;

    @Column(nullable = false)
    private String programName;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private Boolean holiday;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSubstituteDate() {
        return substituteDate;
    }

    public void setSubstituteDate(Date substituteDate) {
        this.substituteDate = substituteDate;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }
}
