package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_program_role")
public class ProgramRole implements Serializable {

    private static final long serialVersionUID = 11L;

    @Id
    @GeneratedValue
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    private String name;
    private String cycle;
    //连续工作时长
    private int workDays;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radia_program_id")
    private RadioProgram radioProgram;

    public ProgramRole() {
    }

    public ProgramRole(String name, String cycle, int workDays, boolean isDeleted, RadioProgram radioProgram) {
        this.name = name;
        this.cycle = cycle;
        this.workDays = workDays;
        this.isDeleted = isDeleted;
        this.radioProgram = radioProgram;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public int getWorkDays() {
        return workDays;
    }

    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public RadioProgram getRadioProgram() {
        return radioProgram;
    }

    public void setRadioProgram(RadioProgram radioProgram) {
        this.radioProgram = radioProgram;
    }

    @Override
    public String toString() {
        return "ProgramRole{" +
                "name='" + name + '\'' +
                ", cycle='" + cycle + '\'' +
                ", workDays=" + workDays +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ProgramRole)) {
            return false;
        }
        ProgramRole temp = (ProgramRole) obj;
        if (name == null) {
            if (temp.name != null) {
                return false;
            }
        } else if (!name.equals(temp.name)) {
            return false;
        }

        if (cycle == null) {
            if (temp.cycle != null) {
                return false;
            }
        } else if (!cycle.equals(temp.cycle)) {
            return false;
        }

        if (isDeleted != temp.isDeleted) {
            return false;
        }
        if (workDays != temp.workDays) {
            return false;
        }
        return true;
    }
}
