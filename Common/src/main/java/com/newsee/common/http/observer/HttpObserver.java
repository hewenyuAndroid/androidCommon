package com.newsee.common.http.observer;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.HttpException;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/3 14:16
 * 说明:
 * ====================================
 */
public abstract class HttpObserver<T> extends BaseObserver<T> {

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onNext(T t) {
        try {
            if (t != null) {
                onSuccess(t);
            } else {
                Type[] types = getSuperClassGenricTypeArray(this.getClass());
                // 如果是 List 会强转失败
                Class<T> clazz = (Class<T>) types[0];
                // 返回的是JavaBean
                t = clazz.newInstance();
                onSuccess(t);
            }
        } catch (ClassCastException e) {
            // 返回的是空数组
            onSuccess((T) new ArrayList<>());
            return;
        } catch (Exception e) {
            onFailure(RESULT_NEW_INSTANCE_ERROR, e);
            return;
        }

        super.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        String errorCode = "";
        if (e instanceof ApiException) {
            errorCode = ((ApiException) e).getErrorCode();
        } else if (e instanceof HttpException) {
            errorCode = HTTP_ERROR;
        } else {
            errorCode = UN_KNOW_ERROR;
        }
        onFailure(errorCode, e);
        super.onError(e);
    }

}
