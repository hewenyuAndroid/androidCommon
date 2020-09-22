package com.newsee.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.newsee.common.global.PermissionManager;
import com.newsee.common.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/17 17:08
 * 说明: Mvp Fragment 基础公共类
 * ====================================
 */
public abstract class BaseFragment<T> extends MvpFragment implements BaseView {

    private Unbinder mUnBinder;

    @Override
    protected void butterKnifeBind() {
        mUnBinder = ButterKnife.bind(this, mRootView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void emptyEvent(String str) {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }


    // region -------------------------------- 权限申请 --------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults, mContext);
    }

    // endregion -----------------------------------------------------------------------

    // region -------------------------------- 通知Fragment更新 --------------------------------

    public void notifyData(T t) {

    }

    // endregion -------------------------------------------------------------------------------


}
