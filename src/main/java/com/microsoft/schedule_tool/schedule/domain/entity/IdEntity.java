package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author kb_jay
 * @time 2018/12/7
 * entity 基类
 **/
@MappedSuperclass
public abstract class IdEntity implements Serializable {

    private static final long serialVersionUID = 1918446199175160468L;
    @Id
    @GeneratedValue
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
