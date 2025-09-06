package com.yuhuan.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CsvUtils {
    private final static Logger LOG = LoggerFactory.getLogger(CsvUtils.class);

    private CsvUtils () {}

    private static String getFilePath (String fileName) {
        return File.separator + fileName + "-" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + ".csv";
    }

    public static void exportCsvTableWithFullPath(String fileName, Iterable<?> contents, String [] columns) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(fileName))))){
            try (CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.EXCEL.withHeader(columns))) {
                csvPrinter.printRecords(contents);
                csvPrinter.flush();
            } catch (IOException e) {
                LOG.error("导出CSV表格失败：", e);
            }
        } catch (IOException e) {
            LOG.error("文件路径错误！！！");
        }
    }

    public static void exportCsvTable(String fileName, Iterable<?> contents, String [] columns) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(getFilePath(fileName)))))){
            try (CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.EXCEL.withHeader(columns))) {
                csvPrinter.printRecords(contents);
                csvPrinter.flush();
            } catch (IOException e) {
                LOG.error("导出CSV表格失败：", e);
            }
        } catch (IOException e) {
            LOG.error("文件路径错误！！！");
        }
    }

    public static CSVParser readCsvTable(String filePath, String[] header) {
        InputStream resourceAsStream = ExcelUtils.class.getClassLoader().getResourceAsStream(filePath);
        if(resourceAsStream == null) {
            LOG.error("文件读取失败，请检查文件放置的位置是否正确！");
            return null;
        }
        return readCsvTable(resourceAsStream, header);
    }

    public static CSVParser readCsvTable(InputStream inputStream, String[] header) {
        CSVFormat csvFormat = CSVFormat.Builder.create().setHeader(header).build();
        try{
            CSVParser csvParser = CSVParser.parse(inputStream, Charset.defaultCharset(), csvFormat);
            return csvParser;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
