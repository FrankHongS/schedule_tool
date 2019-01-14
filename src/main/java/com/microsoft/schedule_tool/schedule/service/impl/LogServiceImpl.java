package com.microsoft.schedule_tool.schedule.service.impl;

import com.microsoft.schedule_tool.schedule.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author kb_jay
 * @time 2019/1/14
 **/
@Service
public class LogServiceImpl implements LogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);

    @Override
    public void log(String value) {
//        LOGGER.trace("doStuff needed more information - {}", value);
//        LOGGER.debug("doStuff needed to debug - {}", value);
//        LOGGER.info("doStuff took input - {}", value);
//        LOGGER.warn("doStuff needed to warn - {}", value);
//        LOGGER.error("doStuff encountered an error with value - {}", value);
        LOGGER.info("schedule - {}",value);
    }

}
