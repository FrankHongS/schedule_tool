package com.microsoft.schedule_tool.util;

import sun.rmi.runtime.Log;

import java.io.*;

/**
 * @author kb_jay
 * @time 2019/1/8
 **/
public class LogUtils {
    private static boolean isOpen = false;
    private static String log_path = "log.txt";

    private LogUtils() {
        File file = new File(log_path);
        if (!file.exists() || (file.exists() && !file.isFile())) {
            try {
                boolean newFile = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static LogUtils instance;

    public static LogUtils getInstance() {
        if (instance == null) {
            synchronized (LogUtils.class) {
                if (instance == null) {
                    instance = new LogUtils();
                }
            }
        }
        return instance;
    }

    public void write(String s) {
        if (!isOpen) {
            return;
        }
        File file = new File(log_path);
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(s);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
