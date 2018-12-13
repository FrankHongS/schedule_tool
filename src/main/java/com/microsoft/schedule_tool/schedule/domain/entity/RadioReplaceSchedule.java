package com.microsoft.schedule_tool.schedule.domain.entity;

import com.fasterxml.jackson.annotation.JacksonInject;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_radio_replace_schedule")
public class RadioReplaceSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
