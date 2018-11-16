package com.microsoft.schedule_tool.util;

import com.microsoft.schedule_tool.entity.Late;
import com.microsoft.schedule_tool.entity.Leave;

import java.util.List;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 */
public class Util {

    public static Float getLeaveDayCount(List<Leave> leaveList) {
        Float sum = 0f;
        for (Leave leave : leaveList) {
            sum += leave.getDayCount();
        }

        return sum;
    }

    public static String getLeaveDesc(Leave leave){

        return Constants.LEAVE[leave.getLeaveType()] +
                ":从" +
                DateUtil.parseDateToString(leave.getFrom()) +
                "到" +
                DateUtil.parseDateToString(leave.getTo())+
                " "+
                Constants.LEAVE_HALF_DESC[leave.getHalfType()==null?0:leave.getHalfType()]+
                " 备注："+leave.getComment();
    }

    public static String getLateDesc(Late late){
        return Constants.LATE[late.getLateType()]+
                ":发生于"+
                DateUtil.parseDateToString(late.getLateDate())+
                " 备注："+late.getComment();
    }
}
