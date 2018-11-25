package com.microsoft.schedule_tool.vo.schedule;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/23/2018
 * E-mail: v-shhong@microsoft.com
 * 
 * 每一周所有的节目与人员映射关系
 */
public class ProgramScheduleTest {

    // K 节目名  V 每周值班人员
    private Map<String,String> programMap;

    public Map<String,String> getProgramMap() {
        return programMap;
    }

    public void setProgramMap(Map<String,String> programMap) {
        this.programMap = programMap;
    }
}
