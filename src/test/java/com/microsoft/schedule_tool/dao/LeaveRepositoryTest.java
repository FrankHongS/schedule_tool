package com.microsoft.schedule_tool.dao;

import com.microsoft.schedule_tool.entity.Leave;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Frank Hon on 2018/11/4
 * E-mail: frank_hon@foxmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LeaveRepositoryTest {

    @Autowired
    private LeaveRepository leaveRepository;

    @Test
    @Ignore
    public void findAllLeaves(){
        List<Leave> leaves=leaveRepository.findAll();
        assertEquals(1,leaves.size());
    }

    @Test
    @Ignore
    public void findByAlias(){
        String alias="v-shhong";
        List<Leave> leaves=leaveRepository.findByAlias(alias);
        assertEquals(1,leaves.size());
    }

    @Test
    @Ignore
    public void insertLeave(){
        Leave leave=new Leave();
        leave.setName("张三");
        leave.setAlias("v-shhong");
        leave.setNormal(true);
        leave.setComment("hello world.....");
        Leave result=leaveRepository.save(leave);
        assertNotNull(result);
    }

    @Test
    @Ignore
    public void deleteLeave(){
        leaveRepository.deleteById(1);
        List<Leave> leaves=leaveRepository.findAll();
        assertEquals(0,leaves.size());
    }

    @Test
    @Ignore
    public void updateLeave(){
        Leave origin=leaveRepository.findById(7).get();
        Leave leave=new Leave();
        leave.setId(origin.getId());
        leave.setName("Frank Hon");
        leave.setAlias("v-shhong");
        leave.setCreatedTime(origin.getCreatedTime());
        leave.setNormal(true);
        leave.setComment("pretty good !");
        Leave result=leaveRepository.save(leave);
        assertNotNull(result);
    }
}