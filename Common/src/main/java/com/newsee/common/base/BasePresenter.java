package com.newsee.common.base;

import com.newsee.common.mvp.IMvpModel;
import com.newsee.common.mvp.IMvpView;
import com.newsee.common.mvp.MvpPresenter;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/17 17:08
 * 说明: Mvp Presenter 基础公共类
 * ====================================
 */
public abstract class BasePresenter<V extends IMvpView, M extends IMvpModel> extends MvpPresenter<V, M> {

}
