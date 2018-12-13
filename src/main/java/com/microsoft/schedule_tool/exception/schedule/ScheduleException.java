package com.microsoft.schedule_tool.exception.schedule;

import com.microsoft.schedule_tool.vo.result.ResultEnum;

/**
 * @author kb_jay
 * @time 2018/12/13
 **/
/**
 * Created by Frank Hon on 12/12/2018
 * E-mail: v-shhong@microsoft.com
 */
public abstract class ScheduleException extends RuntimeException{

    public ScheduleException(String message){
        super(message);
    }

    public abstract int getCode();

}
