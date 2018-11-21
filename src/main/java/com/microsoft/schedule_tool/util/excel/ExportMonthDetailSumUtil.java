package com.microsoft.schedule_tool.util.excel;

import com.microsoft.schedule_tool.util.Constants;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.excel.MonthDetailSum;
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
 * Created by Frank Hon on 11/15/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ExportMonthDetailSumUtil {

    public static void exportMonthDetailSum(HttpServletResponse response, String fileName, java.util.List<MonthDetailSum> monthDetailSumList) throws IOException, ParseException {
        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
        exportMonthDetailSum(response.getOutputStream(),monthDetailSumList);
    }

    private static void exportMonthDetailSum(OutputStream outputStream, java.util.List<MonthDetailSum> monthDetailSumList) throws IOException, ParseException {
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

    private static void buildMonthDetailSumTableHeader(XSSFWorkbook workbook, XSSFSheet sheet, java.util.List<MonthDetailSum> monthDetailSumList) throws ParseException {


        Row titleRow=sheet.createRow(0);

        String month=monthDetailSumList.get(0).getMonth();

        String from=month+"-01";
        String to= DateUtil.parseMonthString(month);
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

    private static void buildMonthDetailSumTableBody(XSSFWorkbook workbook, XSSFSheet sheet, List<MonthDetailSum> monthDetailSumList) throws ParseException {

        String month=monthDetailSumList.get(0).getMonth();

        String from=month+"-01";
        String to=DateUtil.parseMonthString(month);
        int monthDayCount=DateUtil.getDayCountFromDate(from,to);

        for(int i=0;i<monthDetailSumList.size();i++){
            Row row=sheet.createRow(i+1);

            MonthDetailSum monthDetailSum=monthDetailSumList.get(i);
            String name=monthDetailSum.getName();

            Map<Long,String> descMap=monthDetailSum.getDescMap();
            long currDateTime=DateUtil.parseDateString(from).getTime();
            for(int k=0;k<monthDayCount+1;k++){
                Cell cell=row.createCell(k);
                if(k==0){
                    cell.setCellValue(name);
                }else{
                    if(descMap.containsKey(currDateTime)){
                        cell.setCellValue(descMap.get(currDateTime));
                        XSSFCellStyle cellStyle=createCellStyle(workbook, Constants.MONTH_DETAIL_COLOR);
                        cell.setCellStyle(cellStyle);
                    }else{
                        cell.setCellValue("");
                    }
                    currDateTime+=24*60*60*1000;
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
