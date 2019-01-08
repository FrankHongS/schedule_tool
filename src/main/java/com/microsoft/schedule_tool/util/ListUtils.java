package com.microsoft.schedule_tool.util;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author kb_jay
 * @time 2019/1/8
 **/
public class ListUtils {
    public static <T> void removeAll(ArrayList<T> source, ArrayList<T> remove) {
        for (int i = 0; i < remove.size(); i++) {
            Iterator<T> iterator = source.iterator();
            while (iterator.hasNext()) {
                T next = iterator.next();
                if (remove.get(i).equals(next)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }
}
