package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_radio_schedule")
public class RadioSchedule implements Serializable {

    private static final long serialVersionUID = 7L;

    @Id
    @GeneratedValue
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    private Date date;

    private Boolean isHoliday;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private ProgramRole role;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private StationEmployee employee;

    public RadioSchedule() {
    }

    public RadioSchedule(Date date, ProgramRole role, StationEmployee employee) {
        this.date = date;
        this.role = role;
        this.employee = employee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Boolean getHoliday() {
        return isHoliday;
    }

    public void setHoliday(Boolean holiday) {
        isHoliday = holiday;
    }

    @Override
    public String toString() {
        return "RadioSchedule{" +
                "date=" + date +
                ", role=" + role.toString() +
                ", employee=" + employee.toString() +
                '}';
    }
}
