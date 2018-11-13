package com.microsoft.schedule_tool.util;

import com.microsoft.schedule_tool.entity.Leave;

import java.util.List;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 */
public class Util {

    public static int getLeaveDayCount(List<Leave> leaveList) {
        int sum = 0;
        for (Leave leave : leaveList) {
            sum += leave.getDayCount();
        }

        return sum;
    }
}
