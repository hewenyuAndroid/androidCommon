package com.newsee.common.http.result;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/3 13:44
 * 说明:
 * ====================================
 */
public interface IHttpResult<T> {

    boolean isSuccess();

    T getData();

}
