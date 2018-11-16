package com.microsoft.schedule_tool.service;

import com.microsoft.schedule_tool.vo.Attendance;
import com.microsoft.schedule_tool.vo.Pager;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Frank Hon on 11/12/2018
 * E-mail: v-shhong@microsoft.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SumServiceTest {

    @Autowired
    private SumService mSumService;

    @Test
    @Ignore
    public void getSumByPage() {
        Pager<Attendance> attendancePager= mSumService.getSumByPage(0,6);

//        assertEquals(6,attendancePager.getDataList().size());
        assertEquals(10,attendancePager.getCount());
    }
}