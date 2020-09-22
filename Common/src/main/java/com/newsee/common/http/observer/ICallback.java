package com.newsee.common.http.observer;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/3 11:03
 * 说明:
 * ====================================
 */
public interface ICallback<T> {

    void onStart();

    void onSuccess(T t);

    void onFailure(String errorCode, Throwable throwable);

    void onFinish();

}
