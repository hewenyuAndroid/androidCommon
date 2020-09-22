package com.newsee.common.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Map;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/6/28 19:17
 * 说明:
 * ====================================
 */
public interface IDaoSupport<T> {

    String ID = "_id";

    void init(SQLiteDatabase database, Class<T> clazz);

    /**
     * 插入单条数据
     *
     * @param t
     * @return
     */
    long insert(T t);

    /**
     * 批量插入（检测性能可以使用此方法）
     *
     * @param list
     */
    void insert(List<T> list);

    void delete(Map<String, Object> condition);

    void delete(String sql);

    void update(Map<String, Object> updateValue, Map<String, Object> condition);

    void update(String sql);

    List<T> selectAll();

    /**
     * 条件查询
     *
     * @param condition
     * @return
     */
    List<T> select(Map<String, Object> condition);

    /**
     * 条件查询
     *
     * @param condition 查询条件
     * @param limit     第一个值表示从第几位开始查询,第二个值表示查询多少条记录(int[]{3, 10})
     * @param order     根据哪个字段排序(_id)
     * @param isAsc     升序/降序
     * @return
     */
    List<T> select(Map<String, Object> condition, int[] limit, String order, Boolean isAsc);

    /**
     * 输入完整的sql
     *
     * @param sql
     * @return
     */
    List<T> select(String sql);

}
