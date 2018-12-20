package com.microsoft.schedule_tool.util.schedule;

import com.microsoft.schedule_tool.schedule.domain.vo.response.RespReplaceSchedule;
import com.microsoft.schedule_tool.schedule.domain.vo.response.RespSchedule;
import com.microsoft.schedule_tool.util.DateUtil;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * @author kb_jay
 * @time 2018/12/18
 **/
public class ExportExcelUtil {

    //1:导出假期表
    public static void exportHolidayScheduleTable(HttpServletResponse response,
                                                  String from, String to,
                                                  String fileName,
                                                  List<RespSchedule> datas) throws IOException {
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportHolidayScheduleTable(response.getOutputStream(), from, to, datas);
    }

    private static void exportHolidayScheduleTable(ServletOutputStream outputStream, String from, String to, List<RespSchedule> datas) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = "sheet1";
        XSSFSheet sheet = workbook.createSheet(sheetName);

        buildHolidayScheduleTable(sheet, datas, from, to);

        workbook.write(outputStream);
        outputStream.close();

    }

    private static void buildHolidayScheduleTable(XSSFSheet sheet, List<RespSchedule> datas, String from, String to) {
        HashSet<String> roles = new HashSet<>();

        HashSet<String> dates = new HashSet<>();

        //日期排序
        ArrayList<String> ds = new ArrayList<>();
        for (String date : dates) {
            ds.add(date);
        }
        ds.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    return DateUtil.parseDateString(o1).getTime() > DateUtil.parseDateString(o2).getTime() ? 0 : 1;
                } catch (ParseException e) {
                    return 0;
                }
            }
        });

        HashMap<String, String> roleAndDate2employee = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            String role = datas.get(i).programName + "--" + datas.get(i).roleName;
            roles.add(role);
            String date = datas.get(i).date;
            dates.add(date);
            roleAndDate2employee.put(role + date,
                    datas.get(i).name + "(" + datas.get(i).alias + ")");
        }

        XSSFRow row1 = sheet.createRow(0);
        row1.createCell(0);
        for (int i = 0; i < ds.size(); i++) {
            row1.createCell(i + 1).setCellValue(ds.get(i));
        }

        int rowNum = 1;
        for (String role : roles) {
            XSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(role);
            for (int i = 0; i < dates.size(); i++) {
                row.createCell(i + 1).setCellValue(roleAndDate2employee.get(role + ds.get(i)));
            }
            rowNum++;
        }
    }


    //2：导出替班表
    public static void exportReplaceScheduleTable(HttpServletResponse response,
                                                  String fileName,
                                                  String from, String to,
                                                  List<RespReplaceSchedule> datas) throws IOException, ParseException {
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportReplaceScheduleTable(response.getOutputStream(), from, to, datas);
    }

    private static void exportReplaceScheduleTable(ServletOutputStream outputStream,
                                                   String from, String to,
                                                   List<RespReplaceSchedule> datas) throws IOException, ParseException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = "sheet1";
        XSSFSheet sheet = workbook.createSheet(sheetName);

        buildReplaceScheduleTable(sheet, datas, from, to);

        workbook.write(outputStream);
        outputStream.close();

    }

    private static void buildReplaceScheduleTable(XSSFSheet sheet, List<RespReplaceSchedule> datas, String from, String to) throws ParseException {


        HashSet<String> roles = new HashSet<>();

        HashSet<String> dates = new HashSet<>();

        //日期排序
        ArrayList<String> ds = new ArrayList<>();
        for (String date : dates) {
            ds.add(date);
        }
        ds.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    return DateUtil.parseDateString(o1).getTime() > DateUtil.parseDateString(o2).getTime() ? 0 : 1;
                } catch (ParseException e) {
                    return 0;
                }
            }
        });

        HashMap<String, String> roleAndDate2employee = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            String role = datas.get(i).programName + "--" + datas.get(i).roleName;
            roles.add(role);
            String date = datas.get(i).date;
            dates.add(date);
            roleAndDate2employee.put(role + date,
                    datas.get(i).name + "(" + datas.get(i).alias + ")" + "-->" + datas.get(i).replacedEmployeeName + "(" + datas.get(i).replacedEmployeeAlias + ")");
        }

        XSSFRow row1 = sheet.createRow(0);
        row1.createCell(0);
        for (int i = 0; i < ds.size(); i++) {
            row1.createCell(i + 1).setCellValue(ds.get(i));
        }

        int rowNum = 1;
        for (String role : roles) {
            XSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(role);
            for (int i = 0; i < dates.size(); i++) {
                row.createCell(i + 1).setCellValue(roleAndDate2employee.get(role + ds.get(i)));
            }
            rowNum++;
        }
    }

    //3：导出排班表
    public static void exportScheduleTable(HttpServletResponse response, String from, String to, String fileName, List<RespSchedule> datas) throws IOException, ParseException {
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportScheduleTable(response.getOutputStream(), from, to, datas);
    }

    private static void exportScheduleTable(ServletOutputStream outputStream, String from, String to, List<RespSchedule> datas) throws IOException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = "sheet1";
        XSSFSheet sheet = workbook.createSheet(sheetName);

        buildScheduleTable(sheet, datas, from, to);

        workbook.write(outputStream);
        outputStream.close();

    }

    private static void buildScheduleTable(XSSFSheet sheet, List<RespSchedule> datas, String from, String to) throws ParseException {

        XSSFRow row = sheet.createRow(0);
        int dayCountFromDate = DateUtil.getDayCountFromDate(from, to);
        row.createCell(0);
        for (int i = 0; i < dayCountFromDate; i++) {
            Date nextDate = DateUtil.getNextDate(DateUtil.parseDateString(from), i);
            row.createCell(i + 1).setCellValue(DateUtil.parseDateToString(nextDate));
        }

        HashSet<String> roles = new HashSet<>();
        HashMap<String, String> rolesAndData2employee = new HashMap<>();
        for (int i = 0; i < datas.size(); i++) {
            RespSchedule respSchedule = datas.get(i);
            String roleName = respSchedule.programName + "--" + respSchedule.roleName;
            roles.add(roleName);

            rolesAndData2employee.put(roleName + datas.get(i).date, respSchedule.name + "(" + respSchedule.alias + ")");
        }

        int rowNum = 1;
        for (String role : roles) {
            XSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(role);
            for (int i = 0; i < dayCountFromDate; i++) {
                Date nextDate = DateUtil.getNextDate(DateUtil.parseDateString(from), i);
                row1.createCell(i + 1).setCellValue(rolesAndData2employee.get(role +DateUtil.parseDateToString(nextDate)));
            }
            rowNum++;
        }


    }
}
