package com.newsee.common.mvp;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2018/9/17 17:30
 * 说明:
 * ================================================
 */
public interface IMvpPresenter<V extends IMvpView, M extends IMvpModel> {

    void attach(V view);

    void detach();

}
