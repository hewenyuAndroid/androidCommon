package com.newsee.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.newsee.common.R;
import com.newsee.common.adapter.recycler.SimpleRecyclerAdapter;
import com.newsee.common.dialog.AlertDialog;
import com.newsee.common.dialog.DialogManager;
import com.newsee.common.global.PermissionManager;
import com.newsee.common.mvp.MvpFragment;
import com.newsee.common.utils.DensityUtil;
import com.newsee.common.utils.ToastUtil;

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

    // region -------------------------------- Loading --------------------------------

    private AlertDialog mLoadingDialog;

    public void showErrorMsg(Throwable throwable) {
        showErrorMsg("", throwable.getMessage());
    }

    @Override
    public void showErrorMsg(String errorCode, String msg) {
        ToastUtil.show(msg);
    }

    public void showLoading() {
        showLoading("加载中");
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            TextView message = mLoadingDialog.getView(R.id.tv_dialog_message);
            message.setText(msg);
            return;
        }
        mLoadingDialog = DialogManager.getInstance().showLoading(mContext, msg);
    }

    @Override
    public void updateLoadingProgress(long totalProgress, long progress, int totalCount, int currUploadPos) {
        if (mLoadingDialog == null || !mLoadingDialog.isShowing()) {
            showLoading("上传附件...");
        }

        StringBuffer sb = new StringBuffer();
        sb.append("上传附件 ");
        sb.append(String.format("%.0f", (float) (progress / totalProgress) * 100));
        sb.append("%, ");
        sb.append(currUploadPos);
        sb.append("/");
        sb.append(totalCount);

        TextView message = mLoadingDialog.getView(R.id.tv_dialog_message);
        message.setText(sb.toString());
    }

    @Override
    public void closeLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    // endregion ----------------------------------------------------------------------

    // region -------------------------------- 下拉刷新控件 --------------------------------

    protected static final int IS_NO_MORE = -101;

    private int mRefreshType = 0;

    protected void refreshComplete(XRecyclerView recyclerView) {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof SimpleRecyclerAdapter && ((SimpleRecyclerAdapter) adapter).isEmpty()) {
            recyclerView.setLoadingMoreEnabled(false);
        } else if (recyclerView != null) {
            Boolean isNoMore = (Boolean) recyclerView.getTag(IS_NO_MORE);
            if (isNoMore != null) {
                recyclerView.setNoMore(isNoMore);
            }
        } else {
            setRefreshModel(recyclerView);
        }
    }

    /**
     * 初始化下拉刷新
     *
     * @param recyclerView
     * @param columnNum    列数
     * @param refreshType  0:不刷新    1:仅下拉刷新     2:下拉刷新和上拉加载
     */
    protected void initXRecyclerView(XRecyclerView recyclerView, int columnNum, int refreshType) {
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.getDefaultFootView().setLoadingHint("加载中...");
        recyclerView.getDefaultFootView().setNoMoreHint("我是有底线的");
        ViewGroup footView = recyclerView.getDefaultFootView();
        for (int i = 0; i < footView.getChildCount(); i++) {
            View view = footView.getChildAt(i);
            if (view instanceof TextView) {
                view.setPadding(view.getPaddingLeft(), DensityUtil.dp2px(mContext, 18), view.getPaddingRight(), view.getPaddingBottom());
                ((TextView) view).setTextColor(ContextCompat.getColor(mContext, R.color.color_gray_c));
            }
        }
        footView.setPadding(footView.getPaddingLeft(), footView.getPaddingTop(), footView.getPaddingRight(), DensityUtil.dp2px(mContext, 12));
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, columnNum));
        mRefreshType = refreshType;
        setRefreshModel(recyclerView);
    }

    private void setRefreshModel(XRecyclerView recyclerView) {
        switch (mRefreshType) {
            default:
            case 0:
                recyclerView.setPullRefreshEnabled(false);
                recyclerView.setLoadingMoreEnabled(false);
                break;
            case 1:
                recyclerView.setPullRefreshEnabled(true);
                recyclerView.setLoadingMoreEnabled(false);
                break;
            case 2:
                recyclerView.setPullRefreshEnabled(true);
                recyclerView.setLoadingMoreEnabled(true);
                break;
        }
    }

    @Override
    public void onHttpFinish() {

    }

    // endregion -------------------------------------------------------------------------

}
