package com.lxkplus.RandomInit.utils;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtils {

    /**
     * 将二维列表转化为Excel并进行展示
     * @param data 数据
     * @param fileName 文件名
     * @throws NormalErrorException  当数据为空的时候会抛出异常
     * @throws IOException 写入失败
     */
    static public void excelMaker(List<List<Object>> data, String fileName)
            throws NormalErrorException, IOException {
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
        if (data.isEmpty() || data.get(0).isEmpty()) {
            throw new NormalErrorException(ErrorEnum.NotHaveAnyData);
        }

        Sheet sheet1 = sxssfWorkbook.createSheet("sheet1");
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet1.createRow(i);
            for (int j = 0; j < data.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(StringUtils.formatToString(data.get(i).get(j)));
            }
            // 自动设置列宽
            sheet1.autoSizeColumn(i);
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        sxssfWorkbook.write(fos);
        fos.close();
        sxssfWorkbook.dispose();
    }
}
