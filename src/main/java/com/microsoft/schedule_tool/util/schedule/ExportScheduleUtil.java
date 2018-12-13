package com.microsoft.schedule_tool.util.schedule;

import com.microsoft.schedule_tool.util.Constants;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.schedule.ProgramSchedule;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleContainer;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Frank Hon on 11/21/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ExportScheduleUtil {

    public static void exportScheduleTable(HttpServletResponse response, String fileName, ProgramScheduleContainer container) throws IOException, ParseException {
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportScheduleTable(response.getOutputStream(), container);
    }

    private static void exportScheduleTable(OutputStream outputStream, ProgramScheduleContainer container) throws IOException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = "sheet1";
        XSSFSheet sheet = workbook.createSheet(sheetName);

        // 创建表头
        buildScheduleTableHeader(workbook, sheet, container);

        //创建表的主体
        buildScheduleTableBody(workbook, sheet,container);

        workbook.write(outputStream);
        outputStream.close();
    }

    private static void buildScheduleTableHeader(XSSFWorkbook workbook, XSSFSheet sheet, ProgramScheduleContainer container) throws ParseException {
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
                XSSFCellStyle cellStyle=createCellStyle(workbook,Constants.SCHEDULE_ODD_WEEK_COLOR);
                cell.setCellStyle(cellStyle);
            } else {
                if(((day + i - 2) / 7)%2==0){
                    XSSFCellStyle cellStyle=createCellStyle(workbook,Constants.SCHEDULE_ODD_WEEK_COLOR);
                    cell.setCellStyle(cellStyle);
                }
                long fromTime = DateUtil.parseDateString(from).getTime();
                Date date = new Date(fromTime + 24 * 60 * 60 * 1000);
                cell.setCellValue(DateUtil.parseDateToString(date) + "(" + Constants.DAY_OF_WEEK[(day + i - 2) % 7] + ")");
                from = DateUtil.parseDateToString(date);
            }
        }
    }

    private static void buildScheduleTableBody(XSSFWorkbook workbook, XSSFSheet sheet, ProgramScheduleContainer container) throws ParseException {

        String from = container.getFrom();
        String to = container.getTo();

        int day = DateUtil.getDayOfWeek(from);
        int dayCount = DateUtil.getDayCountFromDate(from, to);

        List<ProgramSchedule> programScheduleList=container.getProgramSchedules();

        for(int i=0;i<programScheduleList.size();i++){
            Row row=sheet.createRow(i+1);

            ProgramSchedule programSchedule=programScheduleList.get(i);
            for(int j=0;j<dayCount+1;j++){
                Cell cell=row.createCell(j);
                if(j==0){
                    cell.setCellValue(programSchedule.getProgramName());
                }else{
                    cell.setCellValue(programSchedule.getEmployeeList().get((day+j-2)/7));
                }
            }
        }
    }

    private static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, Color color){
        XSSFCellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setFillForegroundColor(new XSSFColor(color));
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        return cellStyle;
    }

}
