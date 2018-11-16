package com.microsoft.schedule_tool.vo.result;

/**
 * Created by Frank Hon on 11/15/2018
 * E-mail: v-shhong@microsoft.com
 */
public enum ResultEnum {
    SUCCESS(0,"success"),
    UNKNOWN(-1,"unknown error"),
    EMPLOYEE_ALIAS_NULL(20,"employee alias can't be empty"),
    EMPLOYEE_NAME_NULL(21,"employee name can't be empty"),
    EMPLOYEE_ALIAS_EXIST(22,"employee alias existing"),
    EMPLOYEE_SAVE_FAIL(23,"fail to save employee"),
    EMPLOYEE_UPDATE_FAIL(24,"fail to update employee "),
    EMPLOYEE_NOT_EXIST(25,"employee not existing,can't be updated"),
    EMPLOYEE_ID_NOT_EXIST(26,"employee id not existing"),
    EMPLOYEE_DELETE_FAIL(27,"fail to delete employee");

    private int code;

    private String message;

    ResultEnum(int code,String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
