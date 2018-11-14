package com.microsoft.schedule_tool.util;

import java.awt.*;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 */
public interface Constants {

    String[] LEAVE={
            "homebase",
            "病假",
            "年假",
            "事假",
            "婚假",
            "丧假",
            "产假",
            "陪产假",
            "工伤假"
    };

    String[] LATE={
            "迟到",
            "早退",
            "临时外出"
    };

    Color[] COLOR_ARRAY={
            new Color(221,139,71),
            new Color(50,142,78),
            new Color(98,65,203),
            new Color(220,229,39),
            new Color(67,116,225),
            new Color(180,185,171),
            new Color(238,31,26),
            new Color(183,127,81),
            new Color(64,184,200)
    };

    Color MONTH_DETAIL_COLOR=new Color(238,31,26);
}
