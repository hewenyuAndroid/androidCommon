package com.newsee.common.http.observer;

import android.text.TextUtils;

import com.newsee.common.http.result.HttpResult;
import com.newsee.common.http.result.IHttpResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/3 13:52
 * 说明:
 * ====================================
 */
public class RxHelper {

    public static <T> ObservableTransformer<IHttpResult<T>, T> transformer() {
        return new ObservableTransformer<IHttpResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<IHttpResult<T>> observable) {
                return observable.flatMap(new Function<IHttpResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(final IHttpResult<T> result) throws Exception {
                        if (result.isSuccess()) {
                            if (result instanceof HttpResult) {
                                if (!TextUtils.isEmpty(((HttpResult<T>) result).NWRespCode) && null == ((HttpResult) result).Record) {
                                    ((HttpResult) result).Record = new Object();
                                } else if (null != ((HttpResult<T>) result).success && null == ((HttpResult) result).data) {
                                    ((HttpResult) result).data = new Object();
                                }
                            }

                            // 请求服务器数据成功
                            return createObservable(result.getData());

                        } else {

                            // 请求服务器数据失败
                            return Observable.create(new ObservableOnSubscribe<T>() {
                                @Override
                                public void subscribe(ObservableEmitter<T> observableEmitter) throws Exception {

                                    if (result instanceof HttpResult) {
                                        if (null != ((HttpResult<T>) result).NWRespCode) {
                                            // 旧版接口API异常
                                            observableEmitter.onError(new ApiException(((HttpResult<T>) result).NWRespCode, ((HttpResult<T>) result).NWErrMsg));
                                        } else if (null != ((HttpResult<T>) result).resultCode) {
                                            // 验房接口API异常
                                            observableEmitter.onError(new ApiException(((HttpResult<T>) result).resultCode, ((HttpResult<T>) result).resultMsg));
                                        } else if (null != ((HttpResult) result).success) {
                                            // 荟管家接口API异常
                                            observableEmitter.onError(new ApiException(((HttpResult<T>) result).errorCode, ((HttpResult<T>) result).errorMsg));
                                        } else {
                                            // 未知API异常
                                            observableEmitter.onError(new ApiException("-0002", "未知的服务器错误"));
                                        }
                                    } else {
                                        observableEmitter.onError(new ApiException("-0001", "未知的服务器错误"));
                                    }
                                }

                            });

                        }
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }

    private static <T> Observable<T> createObservable(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> observableEmitter) throws Exception {
                observableEmitter.onNext(data);
                observableEmitter.onComplete();
            }
        });
    }

}
