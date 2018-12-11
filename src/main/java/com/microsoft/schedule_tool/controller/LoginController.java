package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.util.ResultUtil;
import com.microsoft.schedule_tool.vo.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Frank Hon on 12/11/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/login")
public class LoginController {


    @PostMapping()
    public Result login(@RequestParam("username")String username,@RequestParam("password")String password){

        if("Frank".equals(username)&&"123456".equals(password)){
            return ResultUtil.success();
        }else{
            throw new RuntimeException("用户名或密码错误！");
        }
    }
}
