package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_radio_schedule")
public class RadioSchedule extends IdEntity {
    private Date date;

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

    @Override
    public String toString() {
        return "RadioSchedule{" +
                "date=" + date +
                ", role=" + role.toString() +
                ", employee=" + employee.toString() +
                '}';
    }
}
