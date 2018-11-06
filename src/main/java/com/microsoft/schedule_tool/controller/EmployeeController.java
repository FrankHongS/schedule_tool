package com.microsoft.schedule_tool.controller;

import com.microsoft.schedule_tool.entity.Employee;
import com.microsoft.schedule_tool.service.EmployeeService;
import com.microsoft.schedule_tool.vo.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/5/2018
 * E-mail: v-shhong@microsoft.com
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService mEmployeeService;

    private static final String KEY="employee";

    @GetMapping
    public Map<String, List<Employee>> getAllEmployees(){

        Map<String, List<Employee>> resultMap=new HashMap<>();

        List<Employee> employeeList=mEmployeeService.getAllEmployees();
        resultMap.put(KEY,employeeList);

        return resultMap;
    }

    @PostMapping
    public Map<String, Employee> saveEmployee(@RequestParam("alias") String alias,
                                              @RequestParam("name") String name){
        Map<String, Employee> resultMap=new HashMap<>();

        Employee employee=new Employee();
        employee.setAlias(alias);
        employee.setName(name);

        Employee result=mEmployeeService.saveEmployee(employee);
        resultMap.put(KEY,result);

        return resultMap;
    }

    @PostMapping("/update")
    public Map<String, Employee> updateEmployee(@RequestParam("id") Integer id,
                                                @RequestParam("alias") String alias,
                                              @RequestParam("name") String name){
        Map<String, Employee> resultMap=new HashMap<>();

        Employee employee=mEmployeeService.updateEmployee(id, alias, name);

        resultMap.put(KEY,employee);
        return resultMap;
    }

    @PostMapping("/delete")
    public Map<String, String> deleteEmployee(@RequestParam("alias") String alias){
        Map<String, String> resultMap=new HashMap<>();

        if(mEmployeeService.deleteEmployee(alias))
            resultMap.put(KEY,"delete is success");
        else
            resultMap.put(KEY,"delete is failure");

        return resultMap;
    }
}
