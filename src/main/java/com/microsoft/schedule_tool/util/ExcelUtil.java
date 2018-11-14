package com.microsoft.schedule_tool.util;

import com.microsoft.schedule_tool.vo.MonthDetailSum;
import com.microsoft.schedule_tool.vo.leavesum.YearSum;
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
import java.util.Map;

/**
 * Created by Frank Hon on 11/13/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ExcelUtil {

    private static final String YEAR_SUFFIX="年汇总";
    private static final String MONTH_SUFFIX="月汇总";

    public static void exportYearSumExcel(HttpServletResponse response, String fileName, List<YearSum> allYearSumList) throws IOException {

        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
        exportYearSumExcel(response.getOutputStream(),allYearSumList);
    }

    private static void exportYearSumExcel(OutputStream outputStream,List<YearSum> allYearSumList) throws IOException {
        XSSFWorkbook workbook=new XSSFWorkbook();

        String sheetName="sheet1";
        XSSFSheet sheet=workbook.createSheet(sheetName);

        // 创建表头
        buildTableHeader(workbook, sheet);

        //创建表的主体
        buildTableBody(workbook, sheet,allYearSumList);

        workbook.write(outputStream);
        outputStream.close();
    }

    private static void buildTableHeader(XSSFWorkbook workbook,XSSFSheet sheet) {
        Row titleRow=sheet.createRow(0);

        for(int i=0;i<13;i++){

            if(i==0){
                for(int j=0;j<Constants.LEAVE.length+Constants.LATE.length+1;j++){

                    Cell cell=titleRow.createCell(j);
                    if(j==0){
                        cell.setCellValue("");
                    }else if(j<=9){
                        cell.setCellValue(Constants.LEAVE[j-1]+YEAR_SUFFIX);
                        cell.setCellStyle(createCellStyle(workbook,Constants.COLOR_ARRAY[j-1]));
                    }else{
                        cell.setCellValue(Constants.LATE[j-10]+YEAR_SUFFIX);
                    }
                }
            }else{
                for(int j=0;j<Constants.LEAVE.length+Constants.LATE.length;j++){
                    Cell cell=titleRow.createCell(12*i+1+j);
                    if(j<9){
                        cell.setCellValue(Constants.LEAVE[j]+i+MONTH_SUFFIX);
                        cell.setCellStyle(createCellStyle(workbook,Constants.COLOR_ARRAY[j]));
                    }else{
                        cell.setCellValue(Constants.LATE[j-9]+i+MONTH_SUFFIX);
                    }
                }
            }
        }
    }

    private static void buildTableBody(XSSFWorkbook workbook,XSSFSheet sheet,List<YearSum> allYearSumList) {
        for(int i=0;i<allYearSumList.size();i++){
            Row row=sheet.createRow(i+1);
            String name=allYearSumList.get(i).getName();

            YearSum yearSum=allYearSumList.get(i);

            for(int j=0;j<Constants.LEAVE.length+Constants.LATE.length+1;j++){
                Cell cell=row.createCell(j);
                if(j==0){
                    cell.setCellValue(name);
                }else if(j<=9){
                    cell.setCellValue(yearSum.getYearSum().get(j-1));
                    // body's cell set foreground color
//                    cell.setCellStyle(createCellStyle(workbook,Constants.COLOR_ARRAY[j-1]));
                }else{
                    cell.setCellValue(yearSum.getYearSum().get(j-1));
                }
            }

            List<List<Float>> monthSum=yearSum.getMonthSum();
            for(int k=0;k<monthSum.size();k++){
                List<Float> eachMonthSum=monthSum.get(k);
                for(int j=0;j<Constants.LEAVE.length+Constants.LATE.length;j++){
                    Cell cell=row.createCell(12*k+13+j);
                    cell.setCellValue(eachMonthSum.get(j));
//                    if(j<9){
//                        cell.setCellStyle(createCellStyle(workbook,Constants.COLOR_ARRAY[j]));
//                    }
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

    public static void exportMonthDetailSum(HttpServletResponse response, String fileName, List<MonthDetailSum> monthDetailSumList) throws IOException, ParseException {
        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
        exportMonthDetailSum(response.getOutputStream(),monthDetailSumList);
    }

    private static void exportMonthDetailSum(OutputStream outputStream,List<MonthDetailSum> monthDetailSumList) throws IOException, ParseException {
        XSSFWorkbook workbook=new XSSFWorkbook();

        String sheetName="sheet1";
        XSSFSheet sheet=workbook.createSheet(sheetName);

        // 创建表头
        buildMonthDetailSumTableHeader(workbook, sheet,monthDetailSumList);

        //创建表的主体
        buildMonthDetailSumTableBody(workbook, sheet,monthDetailSumList);

        workbook.write(outputStream);
        outputStream.close();
    }

    private static void buildMonthDetailSumTableHeader(XSSFWorkbook workbook,XSSFSheet sheet,List<MonthDetailSum> monthDetailSumList) throws ParseException {


        Row titleRow=sheet.createRow(0);

        String month=monthDetailSumList.get(0).getMonth();

        String from=month+"-01";
        String to=DateUtil.parseMonthString(month);
        int monthDayCount=DateUtil.getDayCountFromDate(from,to);

        for(int i=0;i<monthDayCount+1;i++){
            Cell cell=titleRow.createCell(i);
            if(i==0) {
                cell.setCellValue("");
            }else if(i==1){
                cell.setCellValue(from);
            }else{
                long fromTime=DateUtil.parseDateString(from).getTime();
                Date date=new Date(fromTime+24*60*60*1000);
                cell.setCellValue(DateUtil.parseDateToString(date));
                from=DateUtil.parseDateToString(date);
            }
        }

    }

    private static void buildMonthDetailSumTableBody(XSSFWorkbook workbook,XSSFSheet sheet,List<MonthDetailSum> monthDetailSumList) throws ParseException {

        String month=monthDetailSumList.get(0).getMonth();

        String from=month+"-01";
        String to=DateUtil.parseMonthString(month);
        int monthDayCount=DateUtil.getDayCountFromDate(from,to);

        for(int i=0;i<monthDetailSumList.size();i++){
            Row row=sheet.createRow(i+1);
            String name=monthDetailSumList.get(i).getName();

            MonthDetailSum monthDetailSum=monthDetailSumList.get(i);

            Map<Long,String> descMap=monthDetailSum.getDescMap();
            long currDateTime=DateUtil.parseDateString(from).getTime();
            for(int k=0;k<monthDayCount+1;k++){
                Cell cell=row.createCell(k);
                if(k==0){
                    cell.setCellValue(name);
                }else{
                    if(descMap.containsKey(currDateTime)){
                        cell.setCellValue(descMap.get(currDateTime));
                        XSSFCellStyle cellStyle=createCellStyle(workbook,Constants.MONTH_DETAIL_COLOR);
                        cell.setCellStyle(cellStyle);
                    }else{
                        cell.setCellValue("");
                    }
                    currDateTime+=24*60*60*1000;
                }
            }
        }
    }
}
