package com.yuhuan.utils;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExcelUtils {
    private final static Logger LOG = LoggerFactory.getLogger(ExcelUtils.class);

    private ExcelUtils() {}

    public static List<String> readeExcelHeader(InputStream excelInputSteam,
                                                int sheetNumber,
                                                int headerNumber,
                                                int rowStart) throws IOException {
        //要返回的数据
        List<String> headers = new ArrayList<>();
        //生成工作表
        Workbook workbook = WorkbookFactory.create(excelInputSteam);
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        Row header = sheet.getRow(headerNumber);
        DataFormatter dataFormatter = new DataFormatter();
        for (int i = rowStart; i < header.getLastCellNum(); i++) {
            //获取单元格
            Cell cell = header.getCell(i);
            headers.add(dataFormatter.formatCellValue(cell));
        }
        return headers;
    }

    public static List<Map<String, Object>> readeExcelData(String filePath,
                                                           int sheetNumber,
                                                           int headerNumber,
                                                           int rowStart)  {
        InputStream resourceAsStream = ExcelUtils.class.getClassLoader().getResourceAsStream(filePath);
        if(resourceAsStream == null) {
            LOG.error("文件读取失败，请检查文件放置的位置是否正确！");
            return Lists.newArrayList();
        }
        return readeExcelData(resourceAsStream, sheetNumber, headerNumber, rowStart);
    }

    public static List<Map<String, Object>> readeExcelData(InputStream inputStream,
                                                           int sheetNumber,
                                                           int headerNumber,
                                                           int rowStart) {
        //需要的变量以及要返回的数据
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        //生成工作表
        try(Workbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            //最后一行数据
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
            //获取标题信息
            for (int i = 0; i < header.getLastCellNum(); ++i) {
                Cell cell = header.getCell(i);
                headers.add(dataFormatter.formatCellValue(cell));
            }
            //获取内容信息
            for (int i = rowStart; i <= rowEnd; ++i) {
                Row currentRow = sheet.getRow(i);
                if (Objects.isNull(currentRow)) {
                    continue;
                }
                Map<String, Object> dataMap = new HashMap<>();
                for (int j = 0; j < currentRow.getLastCellNum(); ++j) {
                    //将null转化为Blank
                    Cell data = currentRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (Objects.isNull(data)) {     //感觉这个if有点多余
                        dataMap.put(headers.get(j), null);
                    } else {
                        switch (data.getCellType()) {   //不同的类型分别进行存储
                            case STRING:
                                dataMap.put(headers.get(j), data.getRichStringCellValue().getString());
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(data)) {
                                    dataMap.put(headers.get(j), data.getDateCellValue());
                                } else {
                                    dataMap.put(headers.get(j), data.getNumericCellValue());
                                }
                                break;
                            case FORMULA:
                                dataMap.put(headers.get(j), data.getCellFormula());
                                break;
                            case BOOLEAN:
                                dataMap.put(headers.get(j), data.getBooleanCellValue());
                                break;
                            default:
                                dataMap.put(headers.get(j), null);
                        }
                    }
                }
                result.add(dataMap);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void writeExcelFile(String[] columns, List<Map<String, Object>> data, String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOG.error("filePath: {}", filePath);
                throw new RuntimeException(e);
            }
        }
        LOG.info("开始写入表格数据");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            try (OutputStream os = Files.newOutputStream(Paths.get(filePath))) {
                //写入表格标题行
                Row rowHeader = sheet.createRow(0);
                for (int c = 0; c < columns.length; c++) {
                    Cell cell = rowHeader.createCell(c);
                    cell.setCellValue(columns[c]);
                }
                //写入表格数据行
                for (int r = 0; r < data.size(); r++) {
                    Row rowData = sheet.createRow(r + 1);
                    for (int c = 0; c < columns.length; c++) {
                        Cell cell = rowData.createCell(c);
                        Object value = data.get(r).get(columns[c]);
                        if(value instanceof String) {
                            cell.setCellValue((String)value);
                        } else if(value instanceof Double){
                            cell.setCellValue((Double)value);
                        } else if(value instanceof Boolean) {
                            cell.setCellValue((Boolean)value);
                        }

                    }
                }
                workbook.write(os);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.info("写入表格数据成功！");
    }
}
