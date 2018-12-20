package com.microsoft.schedule_tool;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import com.microsoft.schedule_tool.schedule.repository.RadioProgramRepository;
import com.microsoft.schedule_tool.schedule.service.EqualRolesService;
import com.microsoft.schedule_tool.schedule.service.RadioReplaceScheduleService;
import com.microsoft.schedule_tool.schedule.service.RelationRoleAndEmployeeService;
import com.microsoft.schedule_tool.schedule.service.ScheduleSercive;
import com.microsoft.schedule_tool.schedule.service.impl.RadioProgramSeviceImpl;
import com.microsoft.schedule_tool.schedule.service.impl.ScheduleServiceImpl;
import com.microsoft.schedule_tool.service.impl.schedule.ProgramServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleToolApplicationTests {

    @Resource
    private RadioProgramSeviceImpl programService;

    @Resource
    private RadioProgramRepository radioProgramRepository;

    @Autowired
    private RelationRoleAndEmployeeService relationRoleAndEmployeeService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void getAllProgramByStationId() {

    }

    @Test
    public void testSaveSome() {
        long[] longs = programService.saveSome("[\n" +
                " {\"radioStationId\":1,\n" +
                " \"programName\":\"节目1\"},\n" +
                "\n" +
                "{\"radioStationId\":1,\n" +
                " \"programName\":\"节目2\"},\n" +
                " {\"radioStationId\":1,\n" +
                " \"programName\":\"节目3\"}\n" +
                "]");
        for (int i = 0; i < longs.length; i++) {
            System.out.println(longs[i]);
        }
    }
    @Test
    public void save(){
        relationRoleAndEmployeeService.addWorkers2Role(21l,15l,1);
    }
    @Autowired
    private ScheduleSercive scheduleSercive;
    @Test
    public void testSchedule(){
        scheduleSercive.schedule("2019-9-02","2019-12-01");
    }

    @Autowired
    private EqualRolesService equalRolesService;

    @Autowired
    private RadioReplaceScheduleService radioReplaceScheduleService;
    @Test
    public void testMutex(){
        //equalRolesService.addEqualRoles("7326,7327");
        //equalRolesService.getAllEqualRolesGroup();
        radioReplaceScheduleService.addReplace(7324,"2018-10-08",7338);
    }

}
