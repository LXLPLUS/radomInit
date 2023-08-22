package com.lxkplus.RandomInit.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class ExcelWriter {

    @Resource
    ObjectMapper objectMapper;

    /**
     * 将二维列表转化为Excel并进行展示
     * @param data 数据
     * @param filePath 文件名
     * @throws IOException 写入失败
     */
    public void excelMaker(List<List<String>> data, Path filePath) throws IOException {
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();

        Sheet sheet1 = sxssfWorkbook.createSheet("sheet1");
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet1.createRow(i);
            for (int j = 0; j < data.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(objectMapper.convertValue(data.get(i).get(j), String.class));
            }
            // 自动设置列宽
            sheet1.autoSizeColumn(i);
        }
        FileOutputStream fos = new FileOutputStream(String.valueOf(filePath));
        sxssfWorkbook.write(fos);
        fos.close();
        sxssfWorkbook.dispose();
    }
}
