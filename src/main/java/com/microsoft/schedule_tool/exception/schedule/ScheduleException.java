package com.microsoft.schedule_tool.exception.schedule;

import com.microsoft.schedule_tool.vo.result.ResultEnum;

/**
 * @author kb_jay
 * @time 2018/12/13
 **/
public class ScheduleException extends RuntimeException {

    private int code;

    public ScheduleException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public int getCode() {
        return code;
    }

}
