package com.yuhuan.helper;

import com.yuhuan.entity.ColumnAndDataType;

import java.util.List;

public class SqlHelper {
    public final static String QUERY_ALL_DATABASES = "select datname from pg_database;";

    public static String getQueryTablesSql(String dbName) {
        return "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE' and table_catalog='" + dbName +"';";
    }

    public static String getQueryTablesDataSql(String tableName, List<String> columns) {
        return "SELECT " + String.join(",", columns) + " FROM \"" + tableName +"\" limit 50;";
    }

    public static String getQueryTablesDataSql(String tableName) {
        return "SELECT * FROM \"" + tableName +"\" limit 50;";
    }

    public static String getTableColumnsSql(String tableName) {
        return "SELECT COLUMN_NAME,udt_name  FROM information_schema.\"columns\" WHERE table_schema='public' AND TABLE_NAME= '" + tableName + "';";
    }

    public static String getSearchAllSqlString(String tableName, List<ColumnAndDataType> columns, String searchAllText) {
        StringBuilder sb = new StringBuilder("SELECT * FROM \"" + tableName + "\" WHERE ");
        int j = 0;
        for (int i = 0; i < columns.size(); i++) {
            if(columns.get(i).getDataType().equals("varchar")) {
                sb.append(j > 0 ? " or " : "")
                        .append(columns.get(i).getColumnName())
                        .append(" ILIKE '%")
                        .append(searchAllText)
                        .append("%'");
                j++;
            }
            if(i == columns.size() - 1){
                sb.append(";");
            }
        }
        return sb.toString();
    }
}
