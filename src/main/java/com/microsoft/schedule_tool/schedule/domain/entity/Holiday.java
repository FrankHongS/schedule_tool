package com.microsoft.schedule_tool.schedule.domain.entity;


import javax.persistence.Entity;
import java.util.Date;

/**
 * @author kb_jay
 * @time 2018/12/7
 **/
@Entity(name = "tb1_holiday")
public class Holiday extends IdEntity {
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
