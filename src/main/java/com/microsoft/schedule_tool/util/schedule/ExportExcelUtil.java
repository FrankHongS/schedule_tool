package com.microsoft.schedule_tool.util.schedule;

import com.microsoft.schedule_tool.schedule.domain.vo.response.RespReplaceSchedule;
import com.microsoft.schedule_tool.util.DateUtil;
import com.microsoft.schedule_tool.vo.schedule.ProgramScheduleTest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
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





    //2：导出替班表
    public static void exportReplaceScheduleTable(HttpServletResponse response,
                                                  String fileName,
                                                  String from, String to,
                                                  List<RespReplaceSchedule> datas) throws IOException, ParseException {
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportScheduleTable(response.getOutputStream(), from, to, datas);
    }

    private static void exportScheduleTable(ServletOutputStream outputStream,
                                            String from, String to,
                                            List<RespReplaceSchedule> datas) throws IOException, ParseException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = "sheet1";
        XSSFSheet sheet = workbook.createSheet(sheetName);

        buildScheduleTable(sheet, datas, from, to);

        workbook.write(outputStream);
        outputStream.close();

    }

    private static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, Color color) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(new XSSFColor(color));
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        return cellStyle;
    }


    private static void buildScheduleTable(XSSFSheet sheet, List<RespReplaceSchedule> datas, String from, String to) throws ParseException {


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

}
