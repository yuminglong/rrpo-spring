package com.jiebao.platfrom.common.utils;

import com.jiebao.platfrom.attendance.daomain.Record;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.UserService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckExcelUtil {

    public static Map<String, List<String>> excel(MultipartFile file) {  //年考核 题库导入
        List<String> list = new ArrayList<>();//基础工作
        List<String> xgList = new ArrayList<>();//效果
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = null;
            if (judegExcelEdition(filename)) {
                workbook = new XSSFWorkbook(inputStream);//2007
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }
            Sheet sheetAt = workbook.getSheetAt(0);
            Row row = null;
            for (int i = 1; i < sheetAt.getPhysicalNumberOfRows(); i++) {
                row = sheetAt.getRow(i); //对应行
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }
                    cell.setCellType(CellType.STRING);
                    if (j == 0) {
                        list.add(cell.getStringCellValue());
                    } else if (j == 1) {
                        xgList.add(cell.getStringCellValue());
                    }
                }
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("jc", list);
        map.put("xg", xgList);
        return map;
    }

    private static boolean judegExcelEdition(String fileName) {
        int i = fileName.lastIndexOf(".");
        if (fileName.substring(i).equals("xls")) {
            return false;
        } else {
            return true;
        }
    }

    private static void fz(HSSFCellStyle cellStyle, HSSFCell cell1, String name) {
        cell1.setCellValue(name);
        cell1.setCellStyle(cellStyle);
    }

    public static void exportExcel(List<Record> list, HttpServletResponse response, UserService userService) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        //垂直居中
        cellStyle.setVerticalAlignment(cellStyle.getVerticalAlignmentEnum().CENTER);
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        HSSFSheet sheet = workbook.createSheet("考勤表");
        for (int j = 0; j < 47; j++) {
            if (j < 6 || j >= 37) {
                sheet.setColumnWidth(j, 2000);
            } else if (j >= 6 && j < 37) {
                sheet.setColumnWidth(j, 700);
            }
        }
        sheet.setColumnWidth(47, 2500);


        int i = 0;  //标记行
        HSSFRow row1 = sheet.createRow(i++);
        int cells = 0;
        int day = 1;
        HSSFCell cell1 = row1.createCell(cells++);
        fz(cellStyle, cell1, "工号");
        HSSFCell cell2 = row1.createCell(cells++);
        fz(cellStyle, cell2, "姓名");
        HSSFCell cell3 = row1.createCell(cells++);
        fz(cellStyle, cell3, "职名");
        HSSFCell cell4 = row1.createCell(cells++);
        fz(cellStyle, cell4, "岗位挡序");
        HSSFCell cell5 = row1.createCell(cells++);
        fz(cellStyle, cell5, "岗位工资");
        HSSFCell cell6 = row1.createCell(cells++);
        fz(cellStyle, cell6, "技能工资");
        HSSFCell cell7 = row1.createCell(6);
        fz(cellStyle, cell7, "时间日期");
        HSSFCell cell37 = row1.createCell(37);
        fz(cellStyle, cell6, "小时总计数");
        HSSFCell cell46 = row1.createCell(46);
        fz(cellStyle, cell46, "签名");
        CellRangeAddress cellRangeAddress46 = new CellRangeAddress(0, 2, 46, 46);
        sheet.addMergedRegion(cellRangeAddress46);
        HSSFRow row2 = sheet.createRow(i++);  //第2行的
        for (int ii = 6; ii < 37; ii++) {
            HSSFCell cell = row2.createCell(ii);
            fz(cellStyle, cell, "" + (ii - 5));
            CellRangeAddress cellRangeAddress = new CellRangeAddress(1, 2, ii, ii);
            sheet.addMergedRegion(cellRangeAddress);
        }
        HSSFCell cell337 = row2.createCell(37);  // 2排  37行
        fz(cellStyle, cell337, "已工作时间");
        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(1, 1, 37, 40);
        sheet.addMergedRegion(cellRangeAddress1);
        int cellNum = 40;
        HSSFCell cell9 = row2.createCell(cellNum++);
        fz(cellStyle, cell9, "出差");
        CellRangeAddress cellRangeAddress9 = new CellRangeAddress(1, 2, cellNum, cellNum);
        sheet.addMergedRegion(cellRangeAddress9);
        HSSFCell cell11 = row2.createCell(cellNum++);
        fz(cellStyle, cell11, "年休假");
        CellRangeAddress cellRangeAddress11 = new CellRangeAddress(1, 2, cellNum, cellNum);
        sheet.addMergedRegion(cellRangeAddress11);
        HSSFCell cell12 = row2.createCell(cellNum++);
        fz(cellStyle, cell12, "病假");
        CellRangeAddress cellRangeAddress12 = new CellRangeAddress(1, 2, cellNum, cellNum);
        sheet.addMergedRegion(cellRangeAddress12);
        HSSFCell cell13 = row2.createCell(cellNum++);
        fz(cellStyle, cell13, "事假");
        CellRangeAddress cellRangeAddress13 = new CellRangeAddress(1, 2, cellNum, cellNum);
        sheet.addMergedRegion(cellRangeAddress13);
        HSSFCell cell14 = row2.createCell(cellNum++);
        fz(cellStyle, cell14, "婚假");
        CellRangeAddress cellRangeAddress14 = new CellRangeAddress(1, 2, cellNum, cellNum);
        sheet.addMergedRegion(cellRangeAddress9);
        HSSFCell cell15 = row2.createCell(cellNum++);
        fz(cellStyle, cell15, "合计");
        CellRangeAddress cellRangeAddress15 = new CellRangeAddress(1, 2, cellNum, cellNum);
        sheet.addMergedRegion(cellRangeAddress15);


        HSSFRow row3 = sheet.createRow(i++); //第3行
        for (int ii = 0; ii < 3; ii++) {
            row3.createCell(ii + 37);
        }
        int cell3RowNum = 37;
        HSSFCell cel37 = row3.createCell(cell3RowNum++);
        fz(cellStyle, cel37, "计时");
        HSSFCell cell8 = row3.createCell(cell3RowNum++);
        fz(cellStyle, cell8, "计件");
        HSSFCell cell10 = row3.createCell(cell3RowNum++);
        fz(cellStyle, cell10, "加班");

        for (int ii = 0; ii < 6; ii++) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 2, ii, ii);
            sheet.addMergedRegion(cellRangeAddress);
        }


        //内容区
        for (Record record : list
        ) {
            HSSFRow row = sheet.createRow(i);
            row.setHeight((short) (25 * 20));
            for (int j = 0; j < 47; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                if (j == 0) {
                    cell.setCellValue(i + 1);
                }
                if (j == 1) {
                    User user = userService.getById(record.getUserId());
                    cell.setCellValue(user == null ? "无" : user.getUsername());
                }
                if (j == 2) {
                    cell.setCellValue(record.getPosition());
                }
                if (j == 3) {
                    cell.setCellValue(record.getWard());
                }
                if (j == 4) {
                    cell.setCellValue(record.getMoney());
                }
                if (j == 5) {
                    cell.setCellValue(record.getSkillMoney());
                }
                if (j == 6) {
                    cell.setCellValue(record.getDay1());
                }
                if (j == 7) {
                    cell.setCellValue(record.getDay2());
                }
                if (j == 8) {
                    cell.setCellValue(record.getDay3());
                }
                if (j == 9) {
                    cell.setCellValue(record.getDay4());
                }
                if (j == 10) {
                    cell.setCellValue(record.getDay5());
                }
                if (j == 11) {
                    cell.setCellValue(record.getDay6());
                }
                if (j == 12) {
                    cell.setCellValue(record.getDay7());
                }
                if (j == 13) {
                    cell.setCellValue(record.getDay8());
                }
                if (j == 14) {
                    cell.setCellValue(record.getDay9());
                }
                if (j == 15) {
                    cell.setCellValue(record.getDay10());
                }
                if (j == 16) {
                    cell.setCellValue(record.getDay11());
                }
                if (j == 17) {
                    cell.setCellValue(record.getDay12());
                }
                if (j == 18) {
                    cell.setCellValue(record.getDay13());
                }
                if (j == 19) {
                    cell.setCellValue(record.getDay14());
                }
                if (j == 20) {
                    cell.setCellValue(record.getDay15());
                }
                if (j == 21) {
                    cell.setCellValue(record.getDay16());
                }
                if (j == 22) {
                    cell.setCellValue(record.getDay17());
                }
                if (j == 23) {
                    cell.setCellValue(record.getDay18());
                }
                if (j == 24) {
                    cell.setCellValue(record.getDay19());
                }
                if (j == 25) {
                    cell.setCellValue(record.getDay20());
                }
                if (j == 26) {
                    cell.setCellValue(record.getDay21());
                }
                if (j == 27) {
                    cell.setCellValue(record.getDay22());
                }
                if (j == 28) {
                    cell.setCellValue(record.getDay23());
                }
                if (j == 29) {
                    cell.setCellValue(record.getDay24());
                }
                if (j == 30) {
                    cell.setCellValue(record.getDay25());
                }
                if (j == 31) {
                    cell.setCellValue(record.getDay26());
                }
                if (j == 32) {
                    cell.setCellValue(record.getDay27());
                }
                if (j == 33) {
                    cell.setCellValue(record.getDay28());
                }
                if (j == 34) {
                    cell.setCellValue(record.getDay29());
                }
                if (j == 35) {
                    cell.setCellValue(record.getDay30());
                }
                if (j == 36) {
                    cell.setCellValue(record.getDay31());
                }
                if (j == 37) {
                    cell.setCellValue(record.getJiShi());
                }
                if (j == 38) {
                    cell.setCellValue(record.getJiJian());
                }
                if (j == 39) {
                    cell.setCellValue(record.getJiaBan());
                }
                if (j == 40) {
                    cell.setCellValue(record.getChuChai());
                }
                if (j == 41) {
                    cell.setCellValue(record.getNianXiuJia());
                }
                if (j == 42) {
                    cell.setCellValue(record.getBinJia());
                }
                if (j == 43) {
                    cell.setCellValue(record.getShiJia());
                }
                if (j == 44) {
                    cell.setCellValue(record.getHunJia());
                }
                if (j == 45) {
                    cell.setCellValue(record.getHeJi());
                }
//                if (j == 46) {
//                    cell.setCellValue(record.getHeJi());
//                }
            }
            i++;
        }


        response.setContentType("application/ms-excel;charset=utf-8");
//设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=" + "ssd.xls");

        //刷新缓冲

        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
