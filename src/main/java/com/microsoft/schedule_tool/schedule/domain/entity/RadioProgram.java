package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_radio_program")
public class RadioProgram implements Serializable {

    private static final long serialVersionUID = 6L;

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
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radio_station_id")
    private RadioStation radioStation;

    @OneToMany(mappedBy = "radioProgram")
    private List<ProgramRole> programRoles=new ArrayList<>();

    public RadioProgram() {
    }

    public RadioProgram(String name, boolean isDeleted, RadioStation radioStation, List<ProgramRole> programRoles) {
        this.name = name;
        this.isDeleted = isDeleted;
        this.radioStation = radioStation;
        this.programRoles = programRoles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public RadioStation getRadioStation() {
        return radioStation;
    }

    public void setRadioStation(RadioStation radioStation) {
        this.radioStation = radioStation;
    }

    public List<ProgramRole> getProgramRoles() {
        return programRoles;
    }

    public void setProgramRoles(List<ProgramRole> programRoles) {
        this.programRoles = programRoles;
    }

    @Override
    public String toString() {
        return "RadioProgram{" +
                "name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
