package com.ycmm.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by jishubu on 2018/4/23.
 */
public class ExportExcelUtils {

    public static void exportExcel(String title, String fileName, String[] headers, String[] cellName,
                                   List<Map<String, Object>> dataList, OutputStream os) {

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        //----------------标题样式---------------------
        HSSFCellStyle titleStyle = workbook.createCellStyle();        //标题样式
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont ztFont = workbook.createFont();
        ztFont.setItalic(false);                     // 设置字体为斜体字
        ztFont.setColor(HSSFFont.COLOR_NORMAL);            // 将字体设置为“红色”
        ztFont.setFontHeightInPoints((short)18);    // 将字体大小设置为18px
        ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上
        ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //加粗
//      ztFont.setUnderline(Font.U_DOUBLE);         // 添加（Font.U_SINGLE单条下划线/Font.U_DOUBLE双条下划线）
//      ztFont.setStrikeout(true);                  // 是否添加删除线
        titleStyle.setFont(ztFont);


        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        // 产生表格标题行

        setCell(sheet, title, headers, cellName, style, style2, titleStyle, dataList);

        try {
            File file = new File("D:/poi", fileName);
            file.createNewFile();
            workbook.write(new File("d:\\poi\\"+ fileName));
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static void exportExcel2(String title, String fileName, String[] headers, String[] cellName,
                                   List<Map<String, Object>> dataList) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        //----------------标题样式---------------------
        HSSFCellStyle titleStyle = workbook.createCellStyle();        //标题样式
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont ztFont = workbook.createFont();
        ztFont.setItalic(false);                     // 设置字体为斜体字
        ztFont.setColor(HSSFFont.COLOR_NORMAL);            // 将字体设置为“红色”
        ztFont.setFontHeightInPoints((short)18);    // 将字体大小设置为18px
        ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上
        ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //加粗
//      ztFont.setUnderline(Font.U_DOUBLE);         // 添加（Font.U_SINGLE单条下划线/Font.U_DOUBLE双条下划线）
//      ztFont.setStrikeout(true);                  // 是否添加删除线
        titleStyle.setFont(ztFont);

        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        // 产生表格标题行

        setCell(sheet, title, headers, cellName, style, style2, titleStyle, dataList);

        try {
            File file = new File("F:\\Demo\\project\\Qin\\target\\qin", fileName);
            file.createNewFile();
            workbook.write(new File("F:\\Demo\\project\\Qin\\target\\qin", fileName));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void setCell( HSSFSheet sheet, String title, String[] headers, String[] cellName, HSSFCellStyle style,
                                HSSFCellStyle style2, HSSFCellStyle titleStyle, List<Map<String, Object>> dataList) {

        // ----------------------创建第一行---------------
        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        HSSFRow titleRow = sheet.createRow(0);
        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        HSSFCell titleCell = titleRow.createCell(0);
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length-1));
        // 设置单元格内容
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);

        HSSFRow row = sheet.createRow(1);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        String value = null;
        for (int i = 0, j = dataList.size(); i < j; i++) {
            HSSFRow rowc = sheet.createRow(i + 2);
            Map<String, Object> map = dataList.get(i);
            for (int m = 0, n = headers.length; m < n; m++) {
                HSSFCell cell = rowc.createCell(m);
                cell.setCellStyle(style2);
                value = String.valueOf((map.get(cellName[m])));
                cell.setCellValue(value);

            }
        }
    }


}
