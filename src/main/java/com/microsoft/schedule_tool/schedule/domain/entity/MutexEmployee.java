package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_mutex_employee")
public class MutexEmployee implements Serializable {

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "employee_id_1")
    private StationEmployee employee1;
    @ManyToOne
    @JoinColumn(name = "employee_id_2")
    private StationEmployee employee2;

    public MutexEmployee() {
    }

    public MutexEmployee(StationEmployee employee1, StationEmployee employee2) {
        this.employee1 = employee1;
        this.employee2 = employee2;
    }

    public StationEmployee getEmployee1() {
        return employee1;
    }

    public void setEmployee1(StationEmployee employee1) {
        this.employee1 = employee1;
    }

    public StationEmployee getEmployee2() {
        return employee2;
    }

    public void setEmployee2(StationEmployee employee2) {
        this.employee2 = employee2;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
