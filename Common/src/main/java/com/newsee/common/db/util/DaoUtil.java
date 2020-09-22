package com.newsee.common.db.util;

import android.text.TextUtils;

import java.util.Locale;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/6/28 19:16
 * 说明:
 * ====================================
 */
public class DaoUtil {

    private DaoUtil() {

    }

    /**
     * 将JavaBean中的数据类型转换成SQLite数据库中的字段类型
     *
     * @param type
     * @return
     */
    public synchronized static String getColumnType(String type) {
        String value = null;
        type = type.toLowerCase().trim();
        if (type.contains("string")) {
            value = " text";
        } else if (type.contains("int")) {
            value = " integer";
        } else if (type.contains("boolean")) {
            value = " boolean";
        } else if (type.contains("float")) {
            value = " float";
        } else if (type.contains("double")) {
            value = " double";
        } else if (type.contains("char")) {
            value = " varchar";
        } else if (type.contains("long")) {
            value = " long";
        } else {
            throw new RuntimeException("No support db column type: " + type);
        }
        return value;
    }

    /**
     * 获取数据库表的名称
     *
     * @param clazz
     * @return
     */
    public synchronized static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }

}
