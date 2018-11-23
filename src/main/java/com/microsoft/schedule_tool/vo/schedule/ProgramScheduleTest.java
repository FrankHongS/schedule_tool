package com.microsoft.schedule_tool.vo.schedule;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/23/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ProgramScheduleTest {

    // K 节目名  V 每周值班人员
    private List<Map<String,String>> programMapList;

    public List<Map<String, String>> getProgramMapList() {
        return programMapList;
    }

    public void setProgramMapList(List<Map<String, String>> programMapList) {
        this.programMapList = programMapList;
    }
}
