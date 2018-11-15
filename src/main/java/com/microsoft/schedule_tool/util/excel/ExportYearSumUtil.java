package com.microsoft.schedule_tool.util.excel;

import com.microsoft.schedule_tool.util.Constants;
import com.microsoft.schedule_tool.vo.excel.YearSum;
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
import java.util.List;

/**
 * Created by Frank Hon on 11/15/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ExportYearSumUtil {
    private static final String YEAR_SUFFIX="年汇总";
    private static final String MONTH_SUFFIX="月汇总";

    public static void exportYearSumExcel(HttpServletResponse response, String fileName, List<YearSum> allYearSumList) throws IOException {

        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
        exportYearSumExcel(response.getOutputStream(),allYearSumList);
    }

    private static void exportYearSumExcel(OutputStream outputStream, List<YearSum> allYearSumList) throws IOException {
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
                for(int j = 0; j< Constants.LEAVE.length+Constants.LATE.length+1; j++){

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
}
