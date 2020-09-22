package com.newsee.common.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.newsee.common.db.annotation.SqlField;
import com.newsee.common.db.annotation.SqlTable;
import com.newsee.common.db.util.DaoUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/6/28 19:18
 * 说明:
 * ====================================
 */
public class DaoSupport<T> implements IDaoSupport<T> {

    protected SQLiteDatabase mSQLiteDatabase;

    protected Class<T> mClazz;

    protected String mTableName;

    protected Field[] mFields;

    protected static final Object[] mPutMethodArgs = new Object[2];
    protected static final Map<String, Method> mPutMethods = new HashMap<>();

    @Override
    public void init(SQLiteDatabase database, Class<T> clazz) {
        this.mSQLiteDatabase = database;
        this.mClazz = clazz;

        initTable();
    }

    /**
     * 初始化数据库表
     */
    private void initTable() {
        StringBuffer sb = new StringBuffer();

        SqlTable sqlTable = mClazz.getAnnotation(SqlTable.class);
        if (sqlTable == null) {
            throw new RuntimeException("Table name annotation not find!");
        }
        mTableName = sqlTable.value();
        if (TextUtils.isEmpty(mTableName)) {
            mTableName = DaoUtil.getTableName(mClazz);
        }

        sb.append("create table if not exists ")
                .append(mTableName)
                .append("(" + ID + " integer primary key autoincrement, ");

        Field[] fields = mClazz.getDeclaredFields();

        mFields = fields;

        for (Field field : fields) {
            SqlField sqlField = field.getAnnotation(SqlField.class);
            if (sqlField != null) {
                // 设置访问权限
                field.setAccessible(true);
                // 获取列名
                String columnName = sqlField.columnName();
                if (TextUtils.isEmpty(columnName)) {
                    columnName = field.getName();
                }
                // 获取字段类型
                String columnType = sqlField.columnType();
                if (TextUtils.isEmpty(columnType)) {
                    // 将字段类型转换为数据库中的字段类别
                    columnType = DaoUtil.getColumnType(
                            field.getType().getSimpleName());
                }

                sb.append(columnName)
                        .append(" ")
                        .append(columnType)
                        .append(", ");
            }
        }

        sb.delete(sb.length() - 2, sb.length()).append(");");
        String createTableSql = sb.toString();
        Log.e(getClass().getSimpleName(), "创建数据库SQL = " + createTableSql);
        mSQLiteDatabase.execSQL(createTableSql);
    }

    // region ------------------------------------- 增 -------------------------------------

    /**
     * @param t
     * @return 返回数据的Id
     */
    @Override
    public long insert(T t) {
        ContentValues values = contentValuesByObj(t);
        // null  速度比第三方的快一倍左右
        return mSQLiteDatabase.insert(DaoUtil.getTableName(mClazz), null, values);
    }

    @Override
    public void insert(List<T> list) {
        // 开启数据库事务
        mSQLiteDatabase.beginTransaction();
        for (T data : list) {
            insert(data);
        }
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }


    /**
     * 将 Java 对象 转换成 ContentValue
     *
     * @param t
     * @return
     */
    private ContentValues contentValuesByObj(T t) {
        // 第三方的 使用比对一下 了解一下源码
        ContentValues values = new ContentValues();

        // 封装values
        Field[] fields = mClazz.getDeclaredFields();

        for (Field field : fields) {
            SqlField sqlField = field.getAnnotation(SqlField.class);
            if (sqlField == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                // 获取列名
                String columnName = sqlField.columnName();
                if (TextUtils.isEmpty(columnName)) {
                    columnName = field.getName();
                }
                // 获取value
                Object value = field.get(t);

                mPutMethodArgs[0] = columnName;
                mPutMethodArgs[1] = value;

                String filedTypeName = field.getType().getName();
                Method putMethod = mPutMethods.get(filedTypeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put",
                            String.class, value.getClass());
                    mPutMethods.put(filedTypeName, putMethod);
                }

                putMethod.invoke(values, mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }

    // endregion ---------------------------------------------------------------------------

    // region ------------------------------------- 删 -------------------------------------

    @Override
    public void delete(Map<String, Object> condition) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from ");
        sb.append(mTableName);
        String sqlCondition = appendCondition(condition);
        if (!TextUtils.isEmpty(sqlCondition)) {
            sb.append(" where ");
            sb.append(sqlCondition);
        }

        delete(sb.toString());
    }

    @Override
    public void delete(String sql) {
        if (TextUtils.isEmpty(sql)) {
            throw new RuntimeException("Sql is empty!");
        }
        // 开启数据库事务
        mSQLiteDatabase.beginTransaction();
        mSQLiteDatabase.execSQL(sql.toString());
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    // endregion ---------------------------------------------------------------------------

    // region ------------------------------------- 改 -------------------------------------

    @Override
    public void update(Map<String, Object> updateValue, Map<String, Object> condition) {
        if (updateValue == null
                || updateValue.isEmpty()) {
            throw new RuntimeException("Value is empty!");
        }
        StringBuffer sb = new StringBuffer();
        sb.append("update ");
        sb.append(mTableName);
        sb.append(" set ");
        sb.append(appendCondition(updateValue));

        String sqlCondition = appendCondition(condition);
        if (!TextUtils.isEmpty(sqlCondition)) {
            sb.append(" where ");
            sb.append(sqlCondition);
        }

        update(sb.toString());
    }

    @Override
    public void update(String sql) {
        if (TextUtils.isEmpty(sql)) {
            throw new RuntimeException("Sql is empty!");
        }

        // 开启数据库事务
        mSQLiteDatabase.beginTransaction();
        mSQLiteDatabase.execSQL(sql.toString());
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    // endregion ---------------------------------------------------------------------------

    // region ------------------------------------- 查 -------------------------------------

    @Override
    public List<T> selectAll() {
        String sql = "select * from " + mTableName;
        return select(sql);
    }

    @Override
    public List<T> select(Map<String, Object> condition) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ");
        sb.append(mTableName);
        String sqlCondition = appendCondition(condition);
        if (!TextUtils.isEmpty(sqlCondition)) {
            sb.append(" where ");
            sb.append(sqlCondition);
        }

        return select(sb.toString());
    }

    @Override
    public List<T> select(Map<String, Object> condition, int[] limit, String order, Boolean isAsc) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ");
        sb.append(mTableName);

        // 查询条件
        String sqlCondition = appendCondition(condition);
        if (!TextUtils.isEmpty(sqlCondition)) {
            sb.append(" where ");
            sb.append(sqlCondition);
        }

        // 分页
        if (limit != null
                || limit.length == 2) {
            sb.append(" limit ")
                    .append(limit[0])
                    .append(" offset ")
                    .append(limit[1]);
        }

        // 升降序
        if (!TextUtils.isEmpty(order)) {
            sb.append(" order by ")
                    .append(order);
            if (isAsc == null
                    || isAsc) {
                sb.append(" asc");
            } else {
                sb.append(" desc");
            }
        }

        return select(sb.toString());
    }

    @Override
    public List<T> select(String sql) {
        if (TextUtils.isEmpty(sql)) {
            throw new RuntimeException("Sql is empty!");
        }
        List<T> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            T t = cursor2Obj(cursor);
            list.add(t);
        }
        cursor.close();
        return list;
    }

    private synchronized T cursor2Obj(Cursor cursor) {
        T t = null;
        try {
            t = mClazz.newInstance();
            for (Field field : mFields) {
                SqlField sqlField = field.getAnnotation(SqlField.class);
                if (sqlField != null) {
                    field.setAccessible(true);
                    // 获取列名
                    String columnName = sqlField.columnName();
                    if (TextUtils.isEmpty(columnName)) {
                        columnName = field.getName();
                    }
                    int index = cursor.getColumnIndex(columnName);
                    parseColumnType(cursor, t, field, sqlField, index);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There are no empty constructors!");
        }

        return t;
    }

    private void parseColumnType(Cursor cursor, T t, Field field, SqlField sqlField, int index) throws
            IllegalAccessException {
        // 获取字段类型
        String columnType = sqlField.columnType();
        if (TextUtils.isEmpty(columnType)) {
            // 将字段类型转换为数据库中的字段类别
            columnType = DaoUtil.getColumnType(
                    field.getType().getSimpleName());
        }
        columnType = columnType.toLowerCase();
        if (columnType.contains("string")
                || columnType.contains("text")) {
            field.set(t, cursor.getString(index));
        } else if (columnType.contains("int")) {
            field.set(t, cursor.getInt(index));
        } else if (columnType.contains("boolean")) {
            field.set(t, cursor.getInt(index) == 0);
        } else if (columnType.contains("float")) {
            field.set(t, cursor.getFloat(index));
        } else if (columnType.contains("double")) {
            field.set(t, cursor.getDouble(index));
        } else if (columnType.contains("char")
                || columnType.contains("varchar")) {
            field.set(t, cursor.getInt(index));
        } else if (columnType.contains("long")) {
            field.set(t, cursor.getLong(index));
        }
    }

    // endregion ---------------------------------------------------------------------------

    /**
     * 将条件转换成sql的字符串
     *
     * @param condition
     * @return
     */
    private String appendCondition(Map<String, Object> condition) {
        if (condition == null
                || condition.isEmpty()) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        Set<String> set = condition.keySet();
        for (String key : set) {
            Object obj = condition.get(key);
            sb.append(key);
            sb.append(" = ");
            if (obj.getClass().getName().endsWith(String.class.getName())) {
                sb.append("'");
                sb.append(obj);
                sb.append("'");
            } else {
                sb.append(obj);
            }
        }
        return sb.toString();
    }

}
