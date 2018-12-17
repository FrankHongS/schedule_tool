package com.microsoft.schedule_tool.schedule.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author kb_jay
 * @time 2018/12/17
 **/
@Entity(name = "tb1_equel_role")
public class EqualRole {
    private static final long serialVersionUID = 20L;

    @Id
    @GeneratedValue
    protected Long id;

    //不同角色同一个时段分配的人员相同 eg：（1，2，3）；（tip：角色人员以及权重以第一个id为准）

    public String ids;

    public EqualRole() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "EqualRole{" +
                "id=" + id +
                ", ids='" + ids + '\'' +
                '}';
    }
}
