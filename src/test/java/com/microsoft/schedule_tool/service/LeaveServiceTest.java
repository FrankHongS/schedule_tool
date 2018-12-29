package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.dao.LeaveRepository;
import com.microsoft.schedule_tool.entity.Leave;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private LeaveService mLeaveService;

    @Test
    @Ignore
    public void getAllLeavesByAlias() {
        List<Leave> leaves = mLeaveRepository.findByAlias("v-shhong");

        assertEquals(1, leaves.size());
    }

    @Test
    @Ignore
    public void getAllLeavesByDateRangeAndAlias() throws ParseException {
        String smallDate = "2018-11-01";
        String bigDate = "2018-11-20";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(smallDate);
        Date date2 = sdf.parse(bigDate);
        Date date3 = new Date(date2.getTime() + 24 * 60 * 60 * 1000);

        String alias = "v-lijie";

        List<Leave> leaveList = mLeaveRepository.findByCreatedTimeAfterAndCreatedTimeBeforeAndAlias(date1, date3, alias);
        assertEquals(2, leaveList.size());
    }

    @Test
    @Ignore
    public void saveLeave() {
        Leave leave = new Leave();
        leave.setAlias("v-shhong");
        leave.setName("李杰");
//        leave.setLeaveDateRange("2018-08-09 - 2018-08-12");
        leave.setComment("hello 李杰");
        leave.setNormal(false);

        Leave result = mLeaveRepository.save(leave);
        assertNotNull(result);
    }

    @Test
    @Ignore
    public void updateLeave() {
        Integer id = 5;
        Leave result = null;
        if (mLeaveRepository.findById(id).isPresent()) {
            Leave leave = mLeaveRepository.findById(id).get();
//            leave.setLeaveDateRange("2018-08-09 - 2018-08-13");
            leave.setComment("hello world !");
            result = mLeaveRepository.save(leave);
        }

        assertNotNull(result);
    }

    @Test
    @Ignore
    public void deleteLeave() {
        Integer id = 5;
        mLeaveRepository.deleteById(id);

        boolean result = mLeaveRepository.findById(id).isPresent();

        assertTrue(!result);
    }

    @Ignore
    @Test
    public void getAllLeavesOrderByCreatedTime() {
        List<Leave> leaves = mLeaveService.getAllLeavesOrderByCreatedTime(0, 2);
        System.out.println(leaves);
        assertEquals(2, leaves.size());
    }
}