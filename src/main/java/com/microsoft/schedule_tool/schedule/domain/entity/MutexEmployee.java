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

    //互斥id数组 eg：（1，2，3）；
    public String ids;

    public MutexEmployee(String ids) {
        this.ids = ids;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public MutexEmployee() {
    }

    @Override
    public String toString() {
        return "MutexEmployee{" +
                "id=" + id +
                ", ids='" + ids + '\'' +
                '}';
    }
}
