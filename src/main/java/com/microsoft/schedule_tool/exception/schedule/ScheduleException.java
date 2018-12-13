package com.microsoft.schedule_tool.exception.schedule;

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
