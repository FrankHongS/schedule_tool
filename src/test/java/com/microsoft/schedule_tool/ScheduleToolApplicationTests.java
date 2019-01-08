package com.microsoft.schedule_tool;

import com.microsoft.schedule_tool.schedule.domain.entity.RadioProgram;
import com.microsoft.schedule_tool.schedule.repository.RadioProgramRepository;
import com.microsoft.schedule_tool.schedule.service.*;
import com.microsoft.schedule_tool.schedule.service.impl.RadioProgramSeviceImpl;
import com.microsoft.schedule_tool.schedule.service.impl.ScheduleServiceImpl;
import com.microsoft.schedule_tool.schedule.web.HolidayController;
import com.microsoft.schedule_tool.service.impl.schedule.ProgramServiceImpl;
import com.microsoft.schedule_tool.util.DateUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
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
    @Ignore
    public void contextLoads() {
    }

    @Test
    @Ignore
    public void getAllProgramByStationId() {

    }

    @Test
    @Ignore
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
    @Ignore
    public void save() {
        relationRoleAndEmployeeService.addWorkers2Role(21l, 15l, 1);
    }

    @Autowired
    private ScheduleSercive scheduleSercive;

    @Test
    public void testSchedule() {
        scheduleSercive.schedule("2018-7-03", "2018-9-27");
    }

    @Autowired
    private EqualRolesService equalRolesService;

    @Autowired
    private RadioReplaceScheduleService radioReplaceScheduleService;

    @Test
    @Ignore
    public void testMutex() {
        //equalRolesService.addEqualRoles("7326,7327");
        //equalRolesService.getAllEqualRolesGroup();
        radioReplaceScheduleService.addReplace(7324, "2018-10-08", 7338);
    }


    @Test
    public void testReplace() {
        radioReplaceScheduleService.addReplace(7331, "2019-02-13", 7342);

    }

    @Test
    public void testAddHolidayEmp() {
        scheduleSercive.addHolidayEmployee("2019-03-03", 7331, 7341);
    }

    @Test
    public void addSome() {
        radioReplaceScheduleService.addSomeReplace(7331, "2019-02-18 - 2019-02-20", 7341);
    }
//
//    @Test
//    public void testGetSevenHolidayEndDate() {
//        List<Date> sevenHolidayEndDate = scheduleSercive.getSevenHolidayEndDate();
//        for (int i = 0; i < sevenHolidayEndDate.size(); i++) {
//            Date date = sevenHolidayEndDate.get(i);
//            System.out.println(date);
//        }
//    }

    @Test
    public void testDateUtils() throws ParseException {
        Date date = DateUtil.parseDateString("2018-12-29");
        Date date1 = DateUtil.parseDateString("2019-01-09");
        int betweenWeeks = DateUtil.getBetweenWeeks(date, date1);

    }

    @Autowired
    private HolidayController holidayController;

    @Autowired
    private HolidayService holidayService;
    @Test
    public void testHoliay(){
        String p="[\n" +
                " {\"date\":\"2019-02-04\",\"name\":\"年假\"},\n" +
                "{\"date\":\"2019-02-05\",\"name\":\"年假\"},\n" +
                "{\"date\":\"2019-02-06\",\"name\":\"年假\"},\n" +
                "{\"date\":\"2019-02-07\",\"name\":\"年假\"},\n" +
                "{\"date\":\"2019-02-08\",\"name\":\"年假\"},\n" +
                "{\"date\":\"2019-02-09\",\"name\":\"年假\"},\n" +
                "{\"date\":\"2019-02-10\",\"name\":\"年假\"}\n" +
                "]";
        holidayService.addHolidays(p);
    }
}
