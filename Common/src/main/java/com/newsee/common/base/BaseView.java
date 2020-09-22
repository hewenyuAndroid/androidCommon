package com.newsee.common.base;

import com.newsee.common.mvp.IMvpView;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/18 15:31
 * 说明: View层通用的方法
 * ====================================
 */
public interface BaseView extends IMvpView {

    /**
     * 提示错误信息
     *
     * @param errorCode
     * @param msg
     */
    void showErrorMsg(String errorCode, String msg);

    void showLoading(String msg);

    /**
     * 文件上传更新进度
     *
     * @param totalProgress
     * @param progress
     * @param totalCount
     * @param currUploadPos
     */
    void updateLoadingProgress(long totalProgress, long progress, int totalCount, int currUploadPos);

    void closeLoading();

    void onHttpFinish();

}
