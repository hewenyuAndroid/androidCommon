package com.newsee.common.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newsee.common.mvp.proxy.MvpFragmentProxyImpl;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2018/9/17 17:30
 * 说明:
 * ================================================
 */
public abstract class MvpFragment extends Fragment implements IMvpView {

    private MvpFragmentProxyImpl mMvpProxy;

    protected View mRootView;

    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() == 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        mRootView = LayoutInflater.from(getContext()).inflate(getLayoutId(), container, false);
        butterKnifeBind();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMvpProxy = createProxy();
        mMvpProxy.bindPresenter(this);
        mContext = getContext();
        initView();
        initData();
    }

    private MvpFragmentProxyImpl createProxy() {
        if (mMvpProxy == null) {
            mMvpProxy = new MvpFragmentProxyImpl<>();
        }
        return mMvpProxy;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMvpProxy != null) {
            mMvpProxy.unbindPresenter();
        }
    }

    /**
     * 用于ButterKnife绑定视图
     */
    protected void butterKnifeBind() {

    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

}
