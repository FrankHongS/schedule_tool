package com.microsoft.schedule_tool.util.schedule;

import com.microsoft.schedule_tool.entity.schedule.Substitute;
import com.microsoft.schedule_tool.util.Constants;
import com.microsoft.schedule_tool.util.DateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Frank Hon on 11/28/2018
 * E-mail: v-shhong@microsoft.com
 */
public class ExportSubstituteUtil {

    public static void exportScheduleTable(HttpServletResponse response, String fileName, List<Substitute> substituteList) throws IOException, ParseException {
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportScheduleTable(response.getOutputStream(), substituteList);
    }

    private static void exportScheduleTable(OutputStream outputStream, List<Substitute> substituteList) throws IOException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = "sheet1";
        XSSFSheet sheet = workbook.createSheet(sheetName);

        // 创建表头
        buildScheduleTableHeader(workbook, sheet);

        //创建表的主体
        buildScheduleTableBody(workbook, sheet, substituteList);

        workbook.write(outputStream);
        outputStream.close();
    }

    private static void buildScheduleTableHeader(XSSFWorkbook workbook, XSSFSheet sheet) {

        Row titleRow = sheet.createRow(0);

        for (int i = 0; i < Constants.SUBSTITUTE_HEADER.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(Constants.SUBSTITUTE_HEADER[i]);
        }
    }

    private static void buildScheduleTableBody(XSSFWorkbook workbook, XSSFSheet sheet, List<Substitute> substituteList) {

        for (int i = 0; i < substituteList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Substitute substitute = substituteList.get(i);

            for (int j = 0; j < Constants.SUBSTITUTE_HEADER.length; j++) {
                Cell cell = row.createCell(j);

                switch (j) {
                    case 0:
                        cell.setCellValue(substitute.getEmployeeName());
                        break;
                    case 1:
                        cell.setCellValue(substitute.getProgramName());
                        break;
                    case 2:
                        cell.setCellValue(DateUtil.parseDateToString(substitute.getSubstituteDate()));
                        break;
                    case 3:
                        cell.setCellValue(substitute.getHoliday() ? "是" : "否");
                        break;
                    default:
                        break;
                }
            }
        }

    }

}
