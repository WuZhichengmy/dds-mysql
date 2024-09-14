package com.xmu.modules.display_config.utils;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
public class SqlUtil {

    public static String columns(String table) {
        return "select column_name from information_schema.COLUMNS where TABLE_NAME= '" + table + "'" + "and TABLE_SCHEMA = 'data_display_last'";
    }

    public static String count(String table) {
        return "select count(id) as n from " + table;
    }

    public static String getById(Long recordId, String table) {
        return "select * from " + table + " where id = " + recordId;
    }

    public static String category(String table, String xAxis) {
        return "select " + xAxis + " as xAxis,count(" + xAxis + ") as yAxis from " + table + " group by " + xAxis + " order by " + xAxis;
    }

    public static String like(String table, String field, String keyword) {
//        StringBuilder cStr=new StringBuilder();
//        columns.forEach(column->cStr.append(column).append(","));
//        String c=cStr.deleteCharAt(cStr.length()-1).toString();
        if (!field.equals("") && !keyword.equals("")) {
            return "select * from " + table + " where " + field + " like '%" + keyword + "%'";
        }

        if (field.equals("") && keyword.equals("")) {
            return "select * from " + table;
        }
        if (!field.equals("")) {
            return "select id," + field + " from " + table;
        }
        return null;
    }
}
