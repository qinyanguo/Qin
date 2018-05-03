package test;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.io.IOException;


/**
 * Created by jishubu on 2018/4/27.
 */
public class TestExcel {


    public static  void main(String[] args) throws IOException {
        //创建工作簿
        HSSFWorkbook workBook = new HSSFWorkbook();
        //创建工作表  工作表的名字叫helloWorld
        HSSFSheet sheet = workBook.createSheet("helloWorld");
        //创建行,第3行
        HSSFRow row = sheet.createRow(2);
        //创建单元格，操作第三行第三列
        HSSFCell cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("helloWorld");
        String fileName = "ku_" + System.currentTimeMillis() + ".xls";
        File dir = new File("d:\\poi", fileName);
        workBook.write(new File("d:\\poi"));

        workBook.close();//最后记得关闭工作簿
    }

}
