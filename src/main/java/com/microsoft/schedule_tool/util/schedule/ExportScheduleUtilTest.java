package com.microsoft.schedule_tool.util.schedule;

import com.microsoft.schedule_tool.util.Constants;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.util.Util;
import com.microsoft.schedule_tool.vo.schedule.ProgramSchedule;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainer;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainerTest;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleTest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ExportScheduleUtilTest {

    public static void exportScheduleTable(HttpServletResponse response, String fileName, ProgramScheduleContainerTest container) throws IOException, ParseException {
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportScheduleTable(response.getOutputStream(), container);
    }

    private static void exportScheduleTable(OutputStream outputStream, ProgramScheduleContainerTest container) throws IOException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = "sheet1";
        XSSFSheet sheet = workbook.createSheet(sheetName);

        // 创建表头
        buildScheduleTableHeader(workbook, sheet, container);

        //创建表的主体
        buildScheduleTableBody(workbook, sheet, container);

        workbook.write(outputStream);
        outputStream.close();
    }

    private static void buildScheduleTableHeader(XSSFWorkbook workbook, XSSFSheet sheet, ProgramScheduleContainerTest container) throws ParseException {
        Row titleRow = sheet.createRow(0);

        String from = container.getFrom();
        String to = container.getTo();

        int day = DateUtil.getDayOfWeek(from);
        int dayCount = DateUtil.getDayCountFromDate(from, to);

        for (int i = 0; i < dayCount + 1; i++) {
            Cell cell = titleRow.createCell(i);
            if (i == 0) {
                cell.setCellValue("");
            } else if (i == 1) {
                cell.setCellValue(from + "(" + Constants.DAY_OF_WEEK[day - 1] + ")");
                //每一周设置颜色，以此区分每一周
                XSSFCellStyle cellStyle = createCellStyle(workbook, Constants.SCHEDULE_ODD_WEEK_COLOR);
                cell.setCellStyle(cellStyle);
            } else {
                if (((day + i - 2) / 7) % 2 == 0) {
                    XSSFCellStyle cellStyle = createCellStyle(workbook, Constants.SCHEDULE_ODD_WEEK_COLOR);
                    cell.setCellStyle(cellStyle);
                }
                long fromTime = DateUtil.parseDateString(from).getTime();
                Date date = new Date(fromTime + 24 * 60 * 60 * 1000);
                cell.setCellValue(DateUtil.parseDateToString(date) + "(" + Constants.DAY_OF_WEEK[(day + i - 2) % 7] + ")");
                from = DateUtil.parseDateToString(date);
            }
        }
    }

    private static void buildScheduleTableBody(XSSFWorkbook workbook, XSSFSheet sheet, ProgramScheduleContainerTest container) throws ParseException {

        String from = container.getFrom();
        String to = container.getTo();

        int day = DateUtil.getDayOfWeek(from);//第一天为星期几
        int dayCount = DateUtil.getDayCountFromDate(from, to);

        int createdCount = 0;

        List<ProgramScheduleTest> programScheduleList = container.getProgramScheduleList();

        List<Row> rowList=new ArrayList<>();

        for(int k=0;k<programScheduleList.get(0).getProgramMap().keySet().size();k++){
            Row row=sheet.createRow(k+1);
            rowList.add(row);
        }

        for (int i = 0; i < programScheduleList.size(); i++) {


            ProgramScheduleTest programSchedule = programScheduleList.get(i);
            Map<String,Boolean> workInWeekendMap=programSchedule.getWorkInWeekendMap();
            Map<String, String> programMap = programSchedule.getProgramMap();

            int keyIndex=0;
            for (String key : programMap.keySet()) {

                Row row=rowList.get(keyIndex);

                boolean workInWeekend=workInWeekendMap.get(key);

                if (i == 0) {//第一周
                    Cell nameCell = row.createCell(0);
                    nameCell.setCellValue(key);
                }

                if (i == 0) {
                    for (int j = 0; j < 8 - day; j++) {
                        Cell cell = row.createCell(j + 1);
                        if (!workInWeekend){
                            if(j!=7-day&&j!=6-day){
                                writeProgramRoles(cell,programMap.get(key));
                            }
                        }else{
                            writeProgramRoles(cell,programMap.get(key));
                        }
                    }
                } else if (i < programScheduleList.size() - 1) {
                    for (int j = 0; j < 7; j++) {
                        Cell cell = row.createCell(createdCount + j + 1);
                        if(!workInWeekend){
                            if(j!=5&&j!=6){
                                writeProgramRoles(cell,programMap.get(key));
                            }
                        }else{
                            writeProgramRoles(cell,programMap.get(key));
                        }
                    }
                } else if (i == programScheduleList.size() - 1) {
                    for (int j = 0; j < dayCount - createdCount; j++) {
                        Cell cell = row.createCell(createdCount + j + 1);
                        if(!workInWeekend){
                            if(DateUtil.getDayOfWeek(to)==7){
                                if(j!=dayCount - createdCount-1){
                                    writeProgramRoles(cell,programMap.get(key));
                                }
                            }else if(DateUtil.getDayOfWeek(to)==1){
                                if(j!=dayCount - createdCount-1&&j!=dayCount - createdCount-2){
                                    writeProgramRoles(cell,programMap.get(key));
                                }
                            }else{
                                writeProgramRoles(cell,programMap.get(key));
                            }

                        }else{
                            writeProgramRoles(cell,programMap.get(key));
                        }
                    }
                }

                keyIndex++;

            }

            if(i==0){
                createdCount += 8 - day;
            }else if(i<programScheduleList.size()-1){
                createdCount += 7;
            }
        }
    }

    private static void writeProgramRoles(Cell cell,String nameStr){
        String[] nameArray=nameStr.split(",");
        if(nameArray.length==2){
            cell.setCellValue(nameArray[0]+"("+Constants.PROGRAM_ROLES[0]+"),"+nameArray[1]+"("+Constants.PROGRAM_ROLES[1]+")");
        }else{
            cell.setCellValue(nameStr);
        }
    }

    private static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, Color color) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(new XSSFColor(color));
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        return cellStyle;
    }

}
