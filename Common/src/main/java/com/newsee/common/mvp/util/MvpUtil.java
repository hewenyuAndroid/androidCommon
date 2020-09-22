package com.newsee.common.mvp.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2018/9/17 15:08
 * 说明:
 * ================================================
 */
public class MvpUtil {

    public static Type[] getSuperClassGenricTypeArray(Class clazz) {
        Type genType = clazz.getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return params;
    }

}
