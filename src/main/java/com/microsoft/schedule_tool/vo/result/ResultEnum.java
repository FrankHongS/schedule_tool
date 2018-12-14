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
    EMPLOYEE_ALIAS_REPEAT(135, "employee alias repeat"),

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
    PROGRAM_EMPLOYEE_WRONG_SAVE_PARAMS(136, "program_employee_wrong_save_params"),
    EMPLOYEE_FIND_FAILED(138,"employee_find_failed"),

    STATION_NOT_EXTST(123, "station not exist"),

    PROGRAM_ROLE_NAME_IS_NULL(126, "program_role_name_is_null"),
    PROGRAM_ROLE_NAME_REPEAT(127, "program_role_name_repeat"),
    PROGRAM_ROLE_WRONG_CYCLE(128, "program_role_wrong_cycle"),
    PROGRAM_ROLE_SAVE_FAILED(129, "program_role_save_failed"),
    PROGRAM_ROLE_ID_NOT_EXIST(130, "program_role_id_not_exist"),
    PROGRAM_ROLE_REMOVE_FAILED(131, "program_role_remove_failed"),
    PROGRAM_ROLE_WRONG_WORKDAYS(132, "program_role_wrong_workdays"),
    PROGRAM_ROLE_UPDATE_FAILED(133, "program_role_update_failed"),
    PROGRAM_ROLE_FIND_FAILED(134, "program_role_find_failed"),
    PROGRAM_ROLE_WRONG_SAVE_PARAMS(137, "program_role_wrong_save_params"),
    PROGRAM_ROLE_AND_EMPLOYEE_FIND_ERROR(139,"program_role_and_employee_find_error"),
    PROGRAM_ROLE_SAVE_EMPLOYEE_ERROR(140,"program_role_save_employee_error"),
    PROGRAM_ROLE_REMOVE_EMPLOYEE_ERROR(141,"program_role_remove_employee_error"),
    PROGRAM_ROLE_AND_EMPLOYEE_WRONG_RATIO(142,"program_role_and_employee_wrong_ratio"),
    PROGRAM_ROLE_AND_EMPLOYEE_CHANGE_RATIO_ERROR(143,"program_role_and_employee_change_ratio_error"),
    HOLIDAY_PARAMS_ERROR(144,"holiday_params_error"),
    SCHEDULE_ERROT_PLEASE_RETRY(145,"schedule_errot_please_retry"),
    HOLIDAY_ADD_ERROR(146,"holiday_add_error");

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
