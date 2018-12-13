package com.microsoft.schedule_tool.service.schedule;

import com.microsoft.schedule_tool.dao.schedule.ProgramRepository;
import com.microsoft.schedule_tool.entity.schedule.Program;
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
public class ProgramServiceTest {

    @Autowired
    private ProgramRepository mProgramRepository;

    @Test
    @Ignore
    public void getAllPrograms() {
        List<Program> programs=mProgramRepository.findAll();
        assertEquals(1,programs.size());
    }

    @Test
    @Ignore
    public void saveProgram() {
        Program program=new Program();
        program.setName("hahaha");
        Program result=mProgramRepository.save(program);

        assertNotNull(result);
    }

    @Test
    @Ignore
    public void updateProgram() {
        if(mProgramRepository.findById(71).isPresent()){
            Program program=mProgramRepository.findById(71).get();
            program.setName("今日十万加");
            Program result=mProgramRepository.save(program);
            assertNotNull(result);
        }
    }

    @Test
    @Ignore
    public void deleteProgram() {
        if(mProgramRepository.findById(71).isPresent()){
            mProgramRepository.deleteById(71);

            assertFalse(mProgramRepository.findById(71).isPresent());
        }
    }
}