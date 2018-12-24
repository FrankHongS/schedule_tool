package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.management.relation.Role;
import javax.persistence.*;
import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/24
 **/
//排版状态
@Entity
public class ScheduleStates implements Serializable {
    @Id
    public Long id;

    public String year;

    public int week;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    public ProgramRole role;

    public int ratio;

    //"1,2,3"
    public String wait_selected;

}
