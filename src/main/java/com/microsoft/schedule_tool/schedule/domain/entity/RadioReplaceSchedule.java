package com.microsoft.schedule_tool.schedule.domain.entity;

import com.fasterxml.jackson.annotation.JacksonInject;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_radio_replace_schedule")
public class RadioReplaceSchedule extends IdEntity {
    @OneToOne
    @JoinColumn(name = "schedule_id")
    private RadioSchedule radioSchedule;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private StationEmployee employee;

    public RadioReplaceSchedule() {
    }

    public RadioSchedule getRadioSchedule() {
        return radioSchedule;
    }

    public void setRadioSchedule(RadioSchedule radioSchedule) {
        this.radioSchedule = radioSchedule;
    }

    public StationEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(StationEmployee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "RadioReplaceSchedule{" +
                "radioSchedule=" + radioSchedule.toString() +
                ", employee=" + employee.toString() +
                '}';
    }
}
