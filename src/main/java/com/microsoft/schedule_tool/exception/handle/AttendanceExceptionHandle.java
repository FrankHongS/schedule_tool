package com.microsoft.schedule_tool.exception.handle;

import com.microsoft.schedule_tool.exception.EmployeeException;
import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Frank Hon on 11/15/2018
 * E-mail: v-shhong@microsoft.com
 */
@ControllerAdvice
@Component
public class AttendanceExceptionHandle {

    private static final Logger logger=LoggerFactory.getLogger(AttendanceExceptionHandle.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e){

        if(e instanceof EmployeeException){
            EmployeeException employeeException= (EmployeeException) e;
            return ResultUtil.error(employeeException.getCode(),employeeException.getMessage());
        }else{
            logger.info("exception",e);
            return ResultUtil.error(ResultEnum.UNKNOWN.getCode(),ResultEnum.UNKNOWN.getMessage());
        }
    }
}
