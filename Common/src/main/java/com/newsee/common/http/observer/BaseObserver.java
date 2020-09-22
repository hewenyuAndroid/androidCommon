package com.newsee.common.http.observer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/3 14:01
 * 说明:
 * ====================================
 */
public abstract class BaseObserver<T> implements Observer<T>, ICallback<T> {

    /**
     * 返回Data为空数据，同时数据没有无参的构造异常
     */
    public static final String RESULT_NEW_INSTANCE_ERROR = "-1";

    /**
     * 未知异常
     */
    public static final String UN_KNOW_ERROR = "-2";

    /**
     * 网络异常
     */
    public static final String HTTP_ERROR = "-3";

    private Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        onStart();
    }


    @Override
    public void onComplete() {
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        onFinish();
    }

    @Override
    public void onNext(T t) {
//        if (!mDisposable.isDisposed()) {
//            mDisposable.dispose();
//        }
    }

    protected Type[] getSuperClassGenricTypeArray(Class clazz) {
        Type genType = clazz.getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return params;
    }

}
