package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_station_employee")
public class StationEmployee implements Serializable {

    private static final long serialVersionUID = 4L;

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
    @Column(nullable = false)
    private String alias;
    private boolean isDeleted;

    public StationEmployee() {
    }

    public StationEmployee(String name, String alias, boolean isDeleted) {
        this.name = name;
        this.alias = alias;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "StationEmployee{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof StationEmployee)) {
            return false;
        }
        StationEmployee temp = (StationEmployee) obj;
        if (name == null) {
            if (temp.name != null) {
                return false;
            }
        } else if (!name.equals(temp.name)) {
            return false;
        }

        if (alias == null) {
            if (temp.alias != null) {
                return false;
            }
        } else if (!alias.equals(temp.alias)) {
            return false;
        }

        if (isDeleted != temp.isDeleted) {
            return false;
        }
        return true;
    }
}
