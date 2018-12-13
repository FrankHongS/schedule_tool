package com.microsoft.schedule_tool.service.schedule;

import com.microsoft.schedule_tool.entity.schedule.Substitute;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/28/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface SubstituteService {

    List<Substitute> getAllHolidayOrNotHolidaySubstitutes(Boolean isHoliday);

    List<Substitute> getAllSubstitutesInDataRange(Boolean isHoliday,String from,String to);

    Substitute saveSubstitute(String subDate, String subProgram, String subName, Boolean isHoliday);

    Substitute updateSubstitute(Integer id, Date subDate, String subProgram, String subName, Boolean isHoliday);

    boolean deleteSubstitute(Integer id);
}
