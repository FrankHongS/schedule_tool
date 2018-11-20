package com.microsoft.schedule_tool.exception.schedule;

import com.microsoft.schedule_tool.vo.result.ResultEnum;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ProgramException extends RuntimeException{

    private int code;

    public ProgramException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code=resultEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}
