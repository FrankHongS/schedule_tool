package com.microsoft.schedule_tool.service.impl.schedule;

import com.microsoft.schedule_tool.dao.schedule.SubstituteRepository;
import com.microsoft.schedule_tool.entity.schedule.Substitute;
import com.microsoft.schedule_tool.service.schedule.SubstituteService;
import com.microsoft.schedule_tool.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/28/2018
 * E-mail: v-shhong@microsoft.com
 */
@Service
public class SubstituteServiceImpl implements SubstituteService {

    @Autowired
    private SubstituteRepository mSubstituteRepository;

    @Override
    public List<Substitute> getAllHolidayOrNotHolidaySubstitutes(Boolean isHoliday) {
        return mSubstituteRepository.findAllByHoliday(isHoliday);
    }

    @Override
    public List<Substitute> getAllSubstitutesInDataRange(Boolean isHoliday, String from, String to) {

        try {
            Date fromDate=DateUtil.parseDateString(from);
            Date toDate=DateUtil.parseDateString(to);
            return mSubstituteRepository.findAllByHolidayAndSubstituteDateIsBetween(isHoliday,fromDate,toDate);
        } catch (ParseException e) {
            throw new RuntimeException("date format not proper");
        }
    }

    @Transactional
    @Override
    public Substitute saveSubstitute(String subDateStr, String subProgram, String subName, Boolean isHoliday) {

        if (subName == null || "".equals(subName)) {
            throw new RuntimeException("employee name can't be empty");
        }

        if (subProgram == null || "".equals(subProgram)) {
            throw new RuntimeException("program name can't be empty");
        }

        if (subDateStr == null || "".equals(subDateStr)) {
            throw new RuntimeException("substitute date can't be empty");
        }

        Substitute substitute = new Substitute();
        try {
            Date subDate = DateUtil.parseDateString(subDateStr);
            substitute.setSubstituteDate(subDate);
        } catch (ParseException e) {
            throw new RuntimeException("date format not proper");
        }
        substitute.setProgramName(subProgram);
        substitute.setEmployeeName(subName);
        substitute.setHoliday(isHoliday);
        try {
            Substitute result = mSubstituteRepository.save(substitute);

            if(result==null){
                throw new RuntimeException("fail to save substitute");
            }else{
                return result;
            }

        } catch (Exception e) {
            throw new RuntimeException("fail to save substitute");
        }
    }

    @Override
    public Substitute updateSubstitute(Integer id, Date subDate, String subProgram, String subName, Boolean isHoliday) {
        return null;
    }

    @Override
    public boolean deleteSubstitute(Integer id) {
        return false;
    }
}
