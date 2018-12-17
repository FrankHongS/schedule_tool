package com.microsoft.schedule_tool.schedule.domain.vo.response;

/**
 * @author kb_jay
 * @time 2018/12/17
 **/
public class RespEmployeeByRoleId {
    public long id;
    public String name;
    public String alias;
    public int ratio;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }
}
