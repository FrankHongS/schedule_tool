package com.microsoft.schedule_tool.exception;

import com.microsoft.schedule_tool.vo.result.ResultEnum;

/**
 * Created by Frank Hon on 11/15/2018
 * E-mail: v-shhong@microsoft.com
 */
public class EmployeeException extends RuntimeException{

    private int code;

    public EmployeeException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code=resultEnum.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
