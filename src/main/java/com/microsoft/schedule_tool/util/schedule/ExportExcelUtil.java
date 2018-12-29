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
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("节目（角色）");
        row.createCell(1).setCellValue("日期");
        row.createCell(2).setCellValue("员工");

        for (int i = 0; i < datas.size(); i++) {
            RespSchedule respSchedule = datas.get(i);
            XSSFRow row1 = sheet.createRow(i + 1);
            row1.createCell(0).setCellValue(respSchedule.programName + "(" + respSchedule.roleName + ")");
            row1.createCell(1).setCellValue(respSchedule.date);
            row1.createCell(2).setCellValue(respSchedule.name);
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
        XSSFRow row1 = sheet.createRow(0);
        row1.createCell(0).setCellValue("节目（角色）");
        row1.createCell(1).setCellValue("日期");
        row1.createCell(2).setCellValue("替班员工");
        row1.createCell(3).setCellValue("被替换员工");

        for (int i = 0; i < datas.size(); i++) {
            XSSFRow row = sheet.createRow(i + 1);
            RespReplaceSchedule respReplaceSchedule = datas.get(i);
            row.createCell(0).setCellValue(respReplaceSchedule.programName + "(" + respReplaceSchedule.roleName + ")");
            row.createCell(1).setCellValue(respReplaceSchedule.date);
            row.createCell(2).setCellValue(respReplaceSchedule.replacedEmployeeName);
            row.createCell(3).setCellValue(respReplaceSchedule.name);
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


        HashSet<String> roles = new HashSet<>();
        HashMap<String, String> rolesAndData2employee = new HashMap<>();
        for (int i = 0; i < datas.size(); i++) {
            RespSchedule respSchedule = datas.get(i);
            String roleName = respSchedule.programName + "--" + respSchedule.roleName;
            roles.add(roleName);

            rolesAndData2employee.put(roleName + datas.get(i).date, respSchedule.name);
        }

        row.createCell(0);
        int tempIndex = 1;
        for (String role : roles) {
            row.createCell(tempIndex).setCellValue(role);
            tempIndex++;
        }

        int dayCountFromDate = DateUtil.getDayCountFromDate(from, to);

        for (int i = 0; i < dayCountFromDate; i++) {
            XSSFRow row1 = sheet.createRow(i + 1);
            Date nextDate = DateUtil.getNextDate(DateUtil.parseDateString(from), i);
            row1.createCell(0).setCellValue(DateUtil.parseDateToString(nextDate));

            int temp = 1;
            for (String role : roles) {
                row1.createCell(temp).setCellValue(rolesAndData2employee.get(role + DateUtil.parseDateToString(nextDate)));
                temp++;
            }
        }

//        XSSFRow row = sheet.createRow(0);
//        int dayCountFromDate = DateUtil.getDayCountFromDate(from, to);
//        row.createCell(0);
//        for (int i = 0; i < dayCountFromDate; i++) {
//            Date nextDate = DateUtil.getNextDate(DateUtil.parseDateString(from), i);
//            row.createCell(i + 1).setCellValue(DateUtil.parseDateToString(nextDate));
//        }
//
//        HashSet<String> roles = new HashSet<>();
//        HashMap<String, String> rolesAndData2employee = new HashMap<>();
//        for (int i = 0; i < datas.size(); i++) {
//            RespSchedule respSchedule = datas.get(i);
//            String roleName = respSchedule.programName + "--" + respSchedule.roleName;
//            roles.add(roleName);
//
//            rolesAndData2employee.put(roleName + datas.get(i).date, respSchedule.name + "(" + respSchedule.alias + ")");
//        }
//
//        int rowNum = 1;
//        for (String role : roles) {
//            XSSFRow row1 = sheet.createRow(rowNum);
//            row1.createCell(0).setCellValue(role);
//            for (int i = 0; i < dayCountFromDate; i++) {
//                Date nextDate = DateUtil.getNextDate(DateUtil.parseDateString(from), i);
//                row1.createCell(i + 1).setCellValue(rolesAndData2employee.get(role +DateUtil.parseDateToString(nextDate)));
//            }
//            rowNum++;
//        }


    }
}
