package com.newsee.common.global;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.newsee.common.db.DaoSupportFactory;
import com.newsee.common.dialog.DialogManager;
import com.newsee.common.utils.AppUtil;
import com.newsee.common.utils.ToastUtil;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/12 16:29
 * 说明: Common 模块初始化
 * ====================================
 */
public class CommonApplication {

    // region -------------------------------- 单例初始化 --------------------------------

    private static volatile CommonApplication sInstance;

    private static Application sApplication;

    /**
     * 控制debug版本输出日志
     */
    private boolean isDebug = false;

    public static final CommonApplication getInstance() {
        if (sInstance == null) {
            synchronized (CommonApplication.class) {
                if (sInstance == null) {
                    sInstance = new CommonApplication();
                }
            }
        }
        return sInstance;
    }

    private CommonApplication() {

    }

    /**
     * init() 之前需要增加 OkHttp 拦截器
     *
     * @param application
     */
    public void init(Application application) {
        sApplication = application;
        isDebug = AppUtil.isApkDebug(application);

        initLifeCycle();
        ToastUtil.init(application);
        DaoSupportFactory.getInstance()
                .init(application, AppUtil.getAppName(application), "common");

    }

    public Application getApplication() {
        if (sApplication == null) {
            throw new RuntimeException("please call init()");
        }
        return sApplication;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    /**
     * 是否可以输出日志
     *
     * @return
     */
    public boolean canOutputHttpLog() {
        return isDebug;
    }

    // endregion ------------------------------------------------------------------------

    // region -------------------------------- 生命周期 --------------------------------

    private void initLifeCycle() {
        getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppManager.getInstance().attachActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                DialogManager.getInstance().releaseDialogWithContext(activity);
                AppManager.getInstance().detachActivity(activity);
            }
        });
    }

    // endregion ----------------------------------------------------------------------


}
