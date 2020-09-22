package com.newsee.common.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.newsee.common.mvp.proxy.MvpActivityProxyImpl;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2018/9/17 17:30
 * 说明:
 * ================================================
 */
public abstract class MvpActivity extends AppCompatActivity implements IMvpView {

    private MvpActivityProxyImpl mMvpProxy;

    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mMvpProxy = createProxy();
        mMvpProxy.bindPresenter(this);
        mContext = this;

        injectView();

        initView();
        initData();
    }

    private MvpActivityProxyImpl createProxy() {
        if (mMvpProxy == null) {
            // 创建静态代理对象
            mMvpProxy = new MvpActivityProxyImpl<>();
        }
        return mMvpProxy;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMvpProxy != null) {
            mMvpProxy.unbindPresenter();
        }
    }


    /**
     * 注入视图
     */
    protected void injectView() {

    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

}
