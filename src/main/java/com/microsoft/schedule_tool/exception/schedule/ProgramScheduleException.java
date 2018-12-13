package com.microsoft.schedule_tool.exception.schedule;

import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.vo.result.ResultEnum;

/**
 * @author kb_jay
 * @time 2018/12/13
 **/
public class ProgramScheduleException extends ScheduleException {
    private int code;

    public ProgramScheduleException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    @Override
    public int getCode() {
        return code;
    }
}
