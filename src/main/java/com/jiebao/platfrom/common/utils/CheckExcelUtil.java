package com.jiebao.platfrom.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckExcelUtil {

    public static Map<String, List<String>> excel(MultipartFile file) {
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

}
