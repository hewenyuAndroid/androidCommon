package com.newsee.common.global;

import android.app.Activity;

import java.util.Stack;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2019/6/17 14:58
 * 说明: Activity 管理工具类
 * ================================================
 */
public class AppManager {

    private static volatile AppManager sInstance;

    private Stack<Activity> mActivityStack;

    private AppManager() {
        mActivityStack = new Stack<>();
    }

    public static AppManager getInstance() {
        if (sInstance == null) {
            synchronized (AppManager.class) {
                if (sInstance == null) {
                    sInstance = new AppManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 新增一个Activity
     *
     * @param activity
     */
    public void attachActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.add(activity);
        }
    }

    /**
     * 移除一个Activity对象
     *
     * @param activity
     */
    public void detachActivity(Activity activity) {
        if (activity != null && mActivityStack.contains(activity)) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束栈顶的Activity
     */
    public void detachLastActivity() {
        if (mActivityStack.isEmpty()) {
            return;
        }
        detachActivity(mActivityStack.lastElement());
    }

    /**
     * 根据 Class 对象结束单个 Activity
     *
     * @param activityClass
     */
    public void detachActivity(Class<?> activityClass) {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            Activity activity = mActivityStack.get(i);
            if (activity.getClass().getCanonicalName().equals(activityClass.getCanonicalName())) {
                detachActivity(activity);
                break;
            }
        }
    }

    /**
     * 根据 Class 对象结束一类 Activity
     *
     * @param activityClass
     */
    public void detachActivitys(Class<?> activityClass) {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            Activity activity = mActivityStack.get(i);
            if (activity.getClass().getCanonicalName().equals(activityClass.getCanonicalName())) {
                detachActivity(activity);
            }
        }
    }

    /**
     * 移除所有的Activity
     */
    public void detachAllActivity() {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            Activity activity = mActivityStack.get(i);
            detachActivity(activity);
        }
    }

    /**
     * 获取栈顶的Activity
     *
     * @return
     */
    public Activity getLastActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 获取指定类型的Activity
     *
     * @param activityClass
     * @return
     */
    public Activity getActivity(Class<?> activityClass) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().getName().equals(activityClass.getName())) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 获取Activity栈的大小
     *
     * @return
     */
    public int getSize() {
        return mActivityStack.size();
    }

}
