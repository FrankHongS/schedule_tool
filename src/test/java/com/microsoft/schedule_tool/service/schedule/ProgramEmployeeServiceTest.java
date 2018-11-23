package com.microsoft.schedule_tool.service.schedule;

import com.microsoft.schedule_tool.entity.schedule.ProgramEmployee;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Frank Hon on 11/20/2018
 * E-mail: v-shhong@microsoft.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgramEmployeeServiceTest {

    @Autowired
    private ProgramEmployeeService mProgramEmployeeService;

    @Test
    @Ignore
    public void getAllProgramEmployeesByProgramId() {
        List<ProgramEmployee> programEmployeeList=mProgramEmployeeService.getAllProgramEmployeesByProgramId(79);
        assertEquals(1,programEmployeeList.size());
    }

    @Test
    @Ignore
    public void saveProgramEmployee() {
        ProgramEmployee programEmployee=new ProgramEmployee();
        programEmployee.setName("王洛伊");
        Integer programId=79;
        ProgramEmployee result=mProgramEmployeeService.saveProgramEmployee(programEmployee,programId);
        assertNotNull(result);
    }

    @Test
    @Ignore
    public void updateProgramEmployee() {
        List<ProgramEmployee> programEmployeeList=mProgramEmployeeService.getAllProgramEmployeesByProgramId(79);
        ProgramEmployee programEmployee=programEmployeeList.get(0);

        ProgramEmployee result=mProgramEmployeeService.updateProgramEmployee(programEmployee.getId(),"王洛一",0);

        assertNotNull(result);
    }

    @Test
    @Ignore
    public void deleteProgramEmployee() {
        List<ProgramEmployee> programEmployeeList=mProgramEmployeeService.getAllProgramEmployeesByProgramId(79);
        ProgramEmployee programEmployee=programEmployeeList.get(0);
        boolean result=mProgramEmployeeService.deleteProgramEmployee(programEmployee.getId(),0);
        assertTrue(result);
    }
}