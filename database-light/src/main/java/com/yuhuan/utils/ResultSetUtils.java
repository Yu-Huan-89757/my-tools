package com.yuhuan.utils;

import javafx.util.Pair;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetUtils {
    /**
     * 查询结果转换为表格数据，返回Pair结构，left为列名数据集，right为列数据集
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static Pair<List<String>, List<List<Object>>> parseResultSetToList(ResultSet resultSet) throws SQLException {
        List<String> columeNames = new ArrayList<>();
        List<List<Object>> rowDataList = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            columeNames.add(columnName);
        }

        while (resultSet.next()) {
            List<Object> rowData = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                Object columnValue = resultSet.getObject(i);
                rowData.add(columnValue);
            }
            rowDataList.add(rowData);
        }
        return new Pair<>(columeNames, rowDataList);
    }

    /**
     * 查询结果转换为类对象
     * @param resultSet
     * @param clazz
     * @return
     * @param <T>
     * @throws SQLException
     */
    public static <T> List<T> parseResultSetToList(ResultSet resultSet, Class<T> clazz) throws SQLException {
        List<T> resultList = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            T entity;
            try {
                entity = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new SQLException("Failed to create instance of entity class", e);
            }

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);

                try {
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(entity, columnValue);
                } catch (Exception e) {
                    throw new SQLException("Failed to set value for field: " + columnName, e);
                }
            }
            resultList.add(entity);
        }
        return resultList;
    }
}
