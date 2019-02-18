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
                if (next.equals(arr1.get(j))) {
                    iterator.remove();
                    break;
                }
            }
            i++;
        }
    }

    public static <T> void resortArray(ArrayList<T> arr1, ArrayList<T> arr2) {
        int len = arr1.size();
        int count = 0;
        while (count < len) {
            T temp = arr1.get(0);
            if (arr2.contains(temp)) {
                arr1.add(temp);
                arr1.remove(0);
                count++;
            } else {
                break;
            }
        }
    }
}
