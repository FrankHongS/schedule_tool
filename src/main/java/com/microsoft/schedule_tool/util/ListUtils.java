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

    public static <T> void removeDuplicate(ArrayList<T> arr) {
        ArrayList<T> arr1 = new ArrayList<>(arr);
        int i = 0;
        Iterator<T> iterator = arr.iterator();
        while (iterator.hasNext()) {
            if (i == 0) {
                iterator.next();
                i++;
                continue;
            }
            T next = iterator.next();
            for (int j = 0; j < i; j++) {
                if (iterator.equals(arr1.get(j))) {
                    iterator.remove();
                    break;
                }
            }
            i++;
        }
    }
}
