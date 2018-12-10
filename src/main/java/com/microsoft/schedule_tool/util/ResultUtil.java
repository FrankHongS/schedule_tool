package com.microsoft.schedule_tool.util;

import com.microsoft.schedule_tool.vo.result.Result;
import com.microsoft.schedule_tool.vo.result.ResultEnum;
import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank Hon on 11/15/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ResultUtil {

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result<?> error(int code, String message) {
        Result<?> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static <T> Map<String, T> getResultData(String key, T data) {
        HashMap<String, T> d = new HashMap<>();
        d.put(key, data);
        return d;
    }
}
