package com.microsoft.schedule_tool.schedule.domain.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
@Entity(name = "tb1_radio_station")
public class RadioStation implements Serializable {

    private static final long serialVersionUID = 8L;

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

    private boolean isDeleted;

    @OneToMany(mappedBy = "radioStation")
    private List<RadioProgram> radioProgramSet = new ArrayList<>();


    public RadioStation() {
    }

    public RadioStation(String name, boolean isDeleted, List<RadioProgram> radioProgramSet) {
        this.name = name;
        this.isDeleted = isDeleted;
        this.radioProgramSet = radioProgramSet;
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

    public List<RadioProgram> getRadioProgramSet() {
        return radioProgramSet;
    }

    public void setRadioProgramSet(List<RadioProgram> radioProgramSet) {
        this.radioProgramSet = radioProgramSet;
    }

    @Override
    public String toString() {
        return "RadioStation{" +
                "name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
