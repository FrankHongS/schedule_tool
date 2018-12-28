package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.management.relation.Role;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author kb_jay
 * @time 2018/12/24
 **/
//排版状态
/**
 * 1:id   2:当前周的第一天  3：当前周所在的一轮里开始的那一周的第一天 4：roleId
 */
@Entity(name = "tb_schedule_state")
public class ScheduleStates implements Serializable {
    @Id
    public Long id;
    /**
     * 当前的日期
     */
    public Date currentDate;
    /**
     * 一轮开始的日期
     */
    public Date firstDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    public ProgramRole role;

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public ProgramRole getRole() {
        return role;
    }

    public void setRole(ProgramRole role) {
        this.role = role;
    }
}
