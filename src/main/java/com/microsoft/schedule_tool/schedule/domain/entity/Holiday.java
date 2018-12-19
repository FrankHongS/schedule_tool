package com.microsoft.schedule_tool.schedule.domain.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_holiday")
public class Holiday implements Serializable {

    private static final long serialVersionUID = 3L;

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

    public Holiday() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "date=" + date +
                '}';
    }
}
