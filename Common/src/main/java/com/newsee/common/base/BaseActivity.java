package com.newsee.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.newsee.common.global.PermissionManager;
import com.newsee.common.mvp.MvpActivity;
import com.newsee.common.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/17 17:07
 * 说明: Mvp Activity 基础类
 * ====================================
 */
public abstract class BaseActivity extends MvpActivity implements BaseView {

    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);

    }

    // region --------------------------- 状态栏深色字体 ---------------------------

    protected void setStatusBarDark(int backgroundColor) {
        StatusBarUtil.setStatusTextColor(true, this);
        ViewGroup parentView = (ViewGroup) findViewById(android.R.id.content);
        if (parentView != null) {
            parentView.setPadding(parentView.getPaddingLeft(), StatusBarUtil.getStatusBarHeight(mContext), parentView.getPaddingRight(), parentView.getPaddingBottom());
        }
    }

    // endregion -----------------------------------------------------------------

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void emptyEvent(String str) {

    }

    @Override
    protected void injectView() {
        mUnBinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    // region -------------------------------- 权限申请 --------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    // endregion -----------------------------------------------------------------------

    @Override
    public void showErrorMsg(String errorCode, String msg) {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void updateLoadingProgress(long totalProgress, long progress, int totalCount, int currUploadPos) {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void onHttpFinish() {

    }

}
