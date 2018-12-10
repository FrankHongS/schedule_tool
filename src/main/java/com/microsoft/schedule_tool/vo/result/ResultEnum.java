package com.microsoft.schedule_tool.vo.result;

/**
 * Created by Frank Hon on 11/15/2018
 * E-mail: v-shhong@microsoft.com
 */
public enum ResultEnum {
    SUCCESS(0, "success"),
    UNKNOWN(-1, "unknown error"),
    // attendance employee
    EMPLOYEE_ALIAS_NULL(20, "employee alias can't be empty"),
    EMPLOYEE_NAME_NULL(21, "employee name can't be empty"),
    EMPLOYEE_ALIAS_EXIST(22, "employee alias existing"),
    EMPLOYEE_SAVE_FAIL(23, "fail to save employee"),
    EMPLOYEE_UPDATE_FAIL(24, "fail to update employee "),
    EMPLOYEE_NOT_EXIST(25, "employee not existing,can't be updated"),
    EMPLOYEE_ID_NOT_EXIST(26, "employee id not existing"),
    EMPLOYEE_DELETE_FAIL(27, "fail to delete employee"),

    // schedule program
    PROGRAM_NAME_NULL(110, "program name can't be empty"),
    PROGRAM_NAME_EXIST(111, "program name existing"),
    PROGRAM_SAVE_FAIL(112, "fail to save program"),
    PROGRAM_ID_NOT_EXIST(113, "program id not existing"),
    PROGRAM_UPDATE_FAIL(114, "fail to update program"),
    PROGRAM_DELETE_FAIL(115, "fail to delete program"),
    PROGRAM_WRONG_SAVE_PARAMS(124, "program_wrong_save_params"),
    PROGRAM_FIND_FAILED(125, "program_find_failed"),

    // schedule program employees
    PROGRAM_EMPLOYEE_NAME_NULL(116, "employee name can't be empty"),
    PROGRAM_EMPLOYEE_PROGRAM_ID_NULL(117, "program id related to employee can't be empty"),
    PROGRAM_EMPLOYEE_EXIST(118, "the employee is existing"),
    PROGRAM_EMPLOYEE_SAVE_FAIL(119, "fail to save employee"),
    PROGRAM_EMPLOYEE_ID_NOT_EXIST(120, "employee id not existing"),
    PROGRAM_EMPLOYEE_UPDATE_FAIL(121, "fail to update employee"),
    PROGRAM_EMPLOYEE_DELETE_FAIL(122, "fail to delete employee"),

    STATION_NOT_EXTST(123, "station not exist"),
    ;

    private int code;

    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
