package com.microsoft.schedule_tool.vo;

import java.util.List;

/**
 * Created by Frank Hon on 11/12/2018
 * E-mail: v-shhong@microsoft.com
 */
public class Pager<T> {

    private int count;

    private List<T> dataList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
