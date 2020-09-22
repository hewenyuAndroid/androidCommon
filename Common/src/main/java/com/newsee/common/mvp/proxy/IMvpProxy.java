package com.newsee.common.mvp.proxy;

import com.newsee.common.mvp.IMvpView;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2018/9/17 17:58
 * 说明:
 * ================================================
 */
public interface IMvpProxy<V extends IMvpView> {

    /**
     * 绑定 Presenter
     */
    void bindPresenter(V view);

    /**
     * 解绑 Presenter
     */
    void unbindPresenter();

}
