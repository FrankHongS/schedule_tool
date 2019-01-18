package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.print.attribute.standard.MediaSize;
import java.sql.Date;

/**
 * @author kb_jay
 * @time 2019/1/18
 **/
@Entity(name = "tb_last_schedule_time")
public class LastScheduleTime {
    private static final long serialVersionUID = 110L;

    @Id
    @GeneratedValue
    protected Long id;

    private Date lastScheduleData;

    public Date getLastScheduleData() {
        return lastScheduleData;
    }

    public void setLastScheduleData(Date lastScheduleData) {
        this.lastScheduleData = lastScheduleData;
    }
}
