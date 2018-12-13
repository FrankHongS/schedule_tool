package com.microsoft.schedule_tool.dao.schedule;

import com.microsoft.schedule_tool.entity.schedule.Substitute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/28/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface SubstituteRepository extends JpaRepository<Substitute,Integer> {

    List<Substitute> findAllByHoliday(Boolean isHoliday);

    List<Substitute> findAllByHolidayAndSubstituteDateIsBetween(Boolean isHoliday,
                                                                Date from,
                                                                Date to);
}
