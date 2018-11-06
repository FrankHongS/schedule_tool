package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.LeaveType;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Frank Hon on 11/6/2018
 * E-mail: v-shhong@microsoft.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LeaveServiceTest {

    @Autowired
    private LeaveRepository mLeaveRepository;

    @Test
    @Ignore
    public void getAllLeavesByAlias() {
        List<LeaveType> leaves=mLeaveRepository.findByAlias("v-shhong");

        assertEquals(1,leaves.size());
    }

    @Test
    @Ignore
    public void saveLeave() {
        LeaveType leave=new LeaveType();
        leave.setAlias("v-shhong");
        leave.setName("李杰");
        leave.setLeaveDateRange("2018-08-09 - 2018-08-12");
        leave.setComment("hello 李杰");
        leave.setNormal(false);

        LeaveType result=mLeaveRepository.save(leave);
        assertNotNull(result);
    }

    @Test
    @Ignore
    public void updateLeave() {
        Integer id=5;
        LeaveType result=null;
        if(mLeaveRepository.findById(id).isPresent()){
            LeaveType leave=mLeaveRepository.findById(id).get();
            leave.setLeaveDateRange("2018-08-09 - 2018-08-13");
            leave.setComment("hello world !");
            result=mLeaveRepository.save(leave);
        }

        assertNotNull(result);
    }

    @Test
    @Ignore
    public void deleteLeave(){
        Integer id=5;
        mLeaveRepository.deleteById(id);

        boolean result=mLeaveRepository.findById(id).isPresent();

        assertTrue(!result);
    }
}